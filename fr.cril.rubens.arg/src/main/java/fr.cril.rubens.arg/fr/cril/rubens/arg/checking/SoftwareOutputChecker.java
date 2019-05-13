package fr.cril.rubens.arg.checking;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;
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
		final String effectiveReason = instance.getTranslationHistory() != null ?
				reason+" (generation history: "+instance.getTranslationHistory().stream().map(ArgumentationFrameworkTranslation::getDescription).reduce((a,b) -> a+", "+b).orElseThrow()+")" : reason;
		return CheckResult.newError(effectiveReason);
	}

}
