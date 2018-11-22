package fr.cril.rubens.arg.checking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ExtensionSet;

/**
 * A solver output decoder corresponding to the outputs of ICCMA17.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ICCMA17SolverOutputDecoder implements ISolverOutputDecoder {
	
	private static final String ACCEPTANCE_TRUE = "YES";
	
	private static final String ACCEPTANCE_FALSE = "NO";
	
	private static final String NO_EXT = "NO";

	@Override
	public boolean isAcceptanceTrue(final String solverOutput) {
		return normalizeResult(solverOutput).equals(ACCEPTANCE_TRUE);
	}
	
	@Override
	public String getAcceptanceTrue() {
		return ACCEPTANCE_TRUE;
	}

	@Override
	public boolean isAcceptanceFalse(final String solverOutput) {
		return normalizeResult(solverOutput).equals(ACCEPTANCE_FALSE);
	}
	
	@Override
	public String getAcceptanceFalse() {
		return ACCEPTANCE_FALSE;
	}

	@Override
	public boolean isNoExt(final String solverOutput) {
		return normalizeResult(solverOutput).equals(NO_EXT);
	}
	
	@Override
	public String getNoExt() {
		return NO_EXT;
	}

	@Override
	public ArgumentSet readExtension(final String solverOutput) throws SyntaxErrorException {
		final String normalized = normalizeResult(solverOutput);
		final SyntaxErrorException e = new SyntaxErrorException("\""+normalized+"\" is not a valid extension");
		throwOn(e, normalized.length() < 2 || normalized.charAt(0) != '[' || normalized.charAt(normalized.length()-1) != ']');
		if(normalized.length() == 2) {
			return ArgumentSet.getInstance(Collections.emptySet());
		}
		final List<String> result = Arrays.stream(normalized.substring(1, normalized.length()-1).split(",")).collect(Collectors.toList());
		for(final String arg : result) {
			throwOn(e, arg.chars().anyMatch(c -> !Character.isLetterOrDigit(c)));
		}
		return result.stream().map(Argument::getInstance).collect(ArgumentSet.collector());
	}

	@Override
	public ExtensionSet readExtensionSet(final String extensionSet) throws SyntaxErrorException {
		final String normalized = normalizeResult(extensionSet);
		final List<String> strExtensions = readStrExtensionList(normalized);
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
	
	private String normalizeResult(final String result) {
		return result.replaceAll("\\s", "");
	}
	
	private static void throwOn(final SyntaxErrorException e, final boolean test) throws SyntaxErrorException {
		if(test) {
			throw e;
		}
	}
	
}
