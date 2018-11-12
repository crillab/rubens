package fr.cril.rubens.arg.checking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.core.CheckResult;

/**
 * An enum for argumentation solver output checking.
 * 
 * Each member of the enum corresponds to an algorithm dedicated to a query.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public enum SoftwareOutputChecker {

	/** algorithm for the EE query */
	EE(SoftwareOutputChecker::checkEE),

	/** algorithm for the SE query */
	SE(SoftwareOutputChecker::checkSE),

	/** algorithm for the DS query */
	DS(SoftwareOutputChecker::checkDS),

	/** algorithm for the DC query */
	DC(SoftwareOutputChecker::checkDC);
	
	/** a flag indicating the generation history must be appended to the error message when a check fails */
	private static final boolean ADD_HISTORY_TO_ERR = true;

	private final BiFunction<ArgumentationFramework, String, CheckResult> checkingFunction;

	private SoftwareOutputChecker(final BiFunction<ArgumentationFramework, String, CheckResult> checkingFunction) {
		this.checkingFunction = checkingFunction;
	}
	
	/**
	 * Checks a solver output against an abstract argumentation framework.
	 * 
	 * The solver is intended to be run on the instance described in the AF.
	 * This method checks the output of the solver corresponds to what was expected, using the set of extensions in the AF to check the correctness of the solver.
	 * 
	 * @param af the argumentation framework
	 * @param result the solver output
	 * @return a {@link CheckResult} instance indicating the result of the check
	 */
	public CheckResult check(final ArgumentationFramework af, final String result) {
		return this.checkingFunction.apply(af, normalizeResult(result));
	}

	private static CheckResult checkEE(final ArgumentationFramework expected, final String result) {
		try {
			final ExtensionSet ext = readExtensionList(result);
			return expected.getExtensions().equals(ext) ? CheckResult.SUCCESS : newError(expected, "got "+result+"; expected "+expected.getExtensions());
		} catch (SyntaxErrorException e) {
			return newError(expected, e.getMessage());
		}
	}

	private static CheckResult checkDS(final ArgumentationFramework expected, final String result) {
		final Argument arg = expected.getArgUnderDecision();
		if(result.equals("YES")) {
			return expected.getExtensions().stream().allMatch(ext -> ext.contains(arg)) ? CheckResult.SUCCESS : newError(expected, "got \"YES\" for argument "+arg.getName()+"; expected \"NO\"");
		} else if(result.equals("NO")) {
			return expected.getExtensions().stream().anyMatch(ext -> !ext.contains(arg)) ? CheckResult.SUCCESS : newError(expected, "got \"NO\" for argument "+arg.getName()+"; expected \"YES\"");
		} else {
			return newError(expected, "got \""+result+"\"; expected \""+(expected.getExtensions().stream().allMatch(ext -> ext.contains(arg)) ? "YES" : "NO" )+"\"");
		}
	}

	private static CheckResult checkDC(final ArgumentationFramework expected, final String result) {
		final Argument arg = expected.getArgUnderDecision();
		if(result.equals("YES")) {
			return expected.getExtensions().stream().anyMatch(ext -> ext.contains(arg)) ? CheckResult.SUCCESS : newError(expected, "got \"YES\" for argument "+arg.getName()+"; expected \"NO\"");
		} else if(result.equals("NO")) {
			return expected.getExtensions().stream().allMatch(ext -> !ext.contains(arg)) ? CheckResult.SUCCESS : newError(expected, "got \"NO\" for argument "+arg.getName()+"; expected \"YES\"");
		} else {
			return newError(expected, "got \""+result+"\"; expected \""+(expected.getExtensions().stream().anyMatch(ext -> ext.contains(arg)) ? "YES" : "NO" )+"\"");
		}
	}

	private static CheckResult checkSE(final ArgumentationFramework expected, final String result) {
		if(result.equals("NO")) {
			return expected.getExtensions().isEmpty() ? CheckResult.SUCCESS : newError(expected, "got \"NO\"; expected one extension in "+expected.getExtensions());
		}
		try {
			return expected.getExtensions().contains(readExtension(result)) ? CheckResult.SUCCESS : newError(expected, "got "+result+"; expected one extension in "+expected.getExtensions());
		} catch (SyntaxErrorException e) {
			return newError(expected, e.getMessage());
		}
	}

	private static String normalizeResult(final String result) {
		return result.replaceAll("\\s", "");
	}

	private static ExtensionSet readExtensionList(final String list) throws SyntaxErrorException {
		final List<String> strExtensions = readStrExtensionList(list);
		final Set<ArgumentSet> extensions = new HashSet<>();
		for(final String strExt : strExtensions) {
			extensions.add(readExtension(strExt));
		}
		return ExtensionSet.getInstance(extensions);
	}
	
	private static List<String> readStrExtensionList(final String extensionList) throws SyntaxErrorException {
		final char[] chars = extensionList.toCharArray();
		final SyntaxErrorException e = new SyntaxErrorException("\""+extensionList+"\" is not a valid extension set");
		throwOn(e, chars.length < 2 || chars[0] != '[' || chars[chars.length-1] != ']');
		final List<String> exts = new ArrayList<>();
		int startIndex = -1;
		boolean commaExpected = false;
		for(int i=1; i<chars.length-1; ++i) {
			final char c = chars[i];
			if(commaExpected) {
				throwOn(e, c != ',');
				commaExpected = false;
			} else if(c == '[') {
				throwOn(e, startIndex != -1);
				startIndex = i;
			} else if(c == ']') {
				throwOn(e, startIndex == -1);
				exts.add(new String(Arrays.copyOfRange(chars, startIndex, i+1)));
				commaExpected = true;
				startIndex = -1;
			}
		}
		if(startIndex != -1) {
			throw e;
		}
		return exts;
	}
	
	private static void throwOn(final SyntaxErrorException e, final boolean test) throws SyntaxErrorException {
		if(test) {
			throw e;
		}
	}
	
	/**
	 * Returns a new {@link CheckResult} instance for fail check with the given reason.
	 * 
	 * If the dedicated flag is set, the generation history of the instance is added to the reason.
	 * 
	 * @param instance the instance that failed the check
	 * @param reason the reason of the fail
	 * @return the corresponding {@link CheckResult} instance
	 */
	private static CheckResult newError(final ArgumentationFramework instance, final String reason) {
		final String effectiveReason = ADD_HISTORY_TO_ERR && instance.getTranslationHistory() != null ?
				reason+" (generation history: "+instance.getTranslationHistory().stream().map(ArgumentationFrameworkTranslation::getDescription).reduce((a,b) -> a+", "+b).orElseThrow()+")" : reason;
		return CheckResult.newError(effectiveReason);
	}

	private static ArgumentSet readExtension(final String extension) throws SyntaxErrorException {
		final SyntaxErrorException e = new SyntaxErrorException("\""+extension+"\" is not a valid extension");
		throwOn(e, extension.length() < 2 || extension.charAt(0) != '[' || extension.charAt(extension.length()-1) != ']');
		if(extension.length() == 2) {
			return ArgumentSet.getInstance(Collections.emptySet());
		}
		final List<String> result = Arrays.stream(extension.substring(1, extension.length()-1).split(",")).collect(Collectors.toList());
		for(final String arg : result) {
			throwOn(e, arg.chars().anyMatch(c -> !Character.isLetterOrDigit(c)));
		}
		return result.stream().map(Argument::getInstance).collect(ArgumentSet.collector());
	}
	
}
