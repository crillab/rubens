package fr.cril.rubens.arg.checking;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.core.TriFunction;
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

	private final TriFunction<ArgumentationFramework, String, ISolverOutputDecoder, CheckResult> checkingFunction;
	
	private SoftwareOutputChecker(final TriFunction<ArgumentationFramework, String, ISolverOutputDecoder, CheckResult> checkingFunction) {
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
	 * @param outputDecoder the decoder for the solver output
	 * @return a {@link CheckResult} instance indicating the result of the check
	 */
	public CheckResult check(final ArgumentationFramework af, final String result, final ISolverOutputDecoder outputDecoder) {
		return this.checkingFunction.apply(af, result, outputDecoder);
	}

	private static CheckResult checkEE(final ArgumentationFramework expected, final String result, final ISolverOutputDecoder outputDecoder) {
		try {
			final ExtensionSet ext = outputDecoder.readExtensionSet(result);
			return expected.getExtensions().equals(ext) ? CheckResult.SUCCESS : newError(expected, "got "+result+"; expected "+expected.getExtensions());
		} catch (SyntaxErrorException e) {
			return newError(expected, e.getMessage());
		}
	}

	private static CheckResult checkDS(final ArgumentationFramework expected, final String result, final ISolverOutputDecoder outputDecoder) {
		final Argument arg = expected.getArgUnderDecision();
		final String yesStr = outputDecoder.getAcceptanceTrue();
		final String noStr = outputDecoder.getAcceptanceFalse();
		if(outputDecoder.isAcceptanceTrue(result)) {
			return expected.getExtensions().stream().allMatch(ext -> ext.contains(arg)) ? CheckResult.SUCCESS : wrongAcceptanceStatusMsg(arg, expected, yesStr, noStr);
		} else if(outputDecoder.isAcceptanceFalse(result)) {
			return expected.getExtensions().stream().anyMatch(ext -> !ext.contains(arg)) ? CheckResult.SUCCESS : wrongAcceptanceStatusMsg(arg, expected, noStr, yesStr);
		} else {
			return wrongAcceptanceStatusMsg(arg, expected, result, expected.getExtensions().stream().allMatch(ext -> ext.contains(arg)) ? yesStr : noStr);
		}
	}

	private static CheckResult wrongAcceptanceStatusMsg(final Argument arg, final ArgumentationFramework af, final String got, final String expected) {
		return newError(af, "got \""+got+"\" for argument "+arg.getName()+"; expected \""+expected+"\"");
	}

	private static CheckResult checkDC(final ArgumentationFramework expected, final String result, final ISolverOutputDecoder outputDecoder) {
		final Argument arg = expected.getArgUnderDecision();
		final String yesStr = outputDecoder.getAcceptanceTrue();
		final String noStr = outputDecoder.getAcceptanceFalse();
		if(outputDecoder.isAcceptanceTrue(result)) {
			return expected.getExtensions().stream().anyMatch(ext -> ext.contains(arg)) ? CheckResult.SUCCESS : wrongAcceptanceStatusMsg(arg, expected, yesStr, noStr);
		} else if(outputDecoder.isAcceptanceFalse(result)) {
			return expected.getExtensions().stream().allMatch(ext -> !ext.contains(arg)) ? CheckResult.SUCCESS : wrongAcceptanceStatusMsg(arg, expected, noStr, yesStr);
		} else {
			return wrongAcceptanceStatusMsg(arg, expected, result, expected.getExtensions().stream().allMatch(ext -> ext.contains(arg)) ? yesStr : noStr);
		}
	}

	private static CheckResult checkSE(final ArgumentationFramework expected, final String result, final ISolverOutputDecoder outputDecoder) {
		if(outputDecoder.isNoExt(result)) {
			return expected.getExtensions().isEmpty() ? CheckResult.SUCCESS : newError(expected, "got \""+outputDecoder.getNoExt()+"\"; expected one extension in "+expected.getExtensions());
		}
		try {
			return expected.getExtensions().contains(outputDecoder.readExtension(result)) ? CheckResult.SUCCESS : newError(expected, "got "+result+"; expected one extension in "+expected.getExtensions());
		} catch (SyntaxErrorException e) {
			return newError(expected, e.getMessage());
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

}
