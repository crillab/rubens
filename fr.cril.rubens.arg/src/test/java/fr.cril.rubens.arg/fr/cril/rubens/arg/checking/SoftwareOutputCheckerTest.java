package fr.cril.rubens.arg.checking;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;

class SoftwareOutputCheckerTest {
	
	@BeforeEach
	public void setUp() {
		Forget.all();
	}
	
	private ArgumentationFramework toAF(final String[]... argSets) {
		final ExtensionSet exts = Arrays.stream(argSets).map(set -> Arrays.stream(set).map(Argument::getInstance).collect(ArgumentSet.collector())).collect(ExtensionSet.collector());
		final ArgumentSet arguments = ArgumentSet.getInstance(Stream.of("a", "b", "c").map(Argument::getInstance).collect(Collectors.toSet()));
		return new ArgumentationFramework(arguments, AttackSet.getInstance(Collections.emptySet()), exts);
	}
	
	
	@Test
	void testEmptyEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(), "[]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSingleEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}), "[[]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testDualEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "[[],[a]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseEE() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "[[],[b]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testWrongValEE() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testEmptySE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(), "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testNoOnNonEmptySE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSingleArgSE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "[a]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testDualArgSE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a", "b"}), "[a,b]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseSE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "[b]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testWrongValSE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a", "b"}), "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testYesDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertSuccess(SoftwareOutputChecker.DC.check(af, "YES", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testNoDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("c"));
		assertSuccess(SoftwareOutputChecker.DC.check(af, "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseYesDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("c"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "YES", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseNoDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testWrongValDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testYesDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertSuccess(SoftwareOutputChecker.DS.check(af, "YES", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testNoDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertSuccess(SoftwareOutputChecker.DS.check(af, "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseYesDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "YES", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testFalseNoDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "NO", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testWrongValDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "foo", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSE3args() {
		final ArgumentationFramework af = toAF(new String[]{"a0", "a1", "a2"});
		assertSuccess(SoftwareOutputChecker.SE.check(af, "[a1,a2,a0]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testEE1ext3args() {
		final ArgumentationFramework af = toAF(new String[]{"a0", "a1", "a2"});
		assertSuccess(SoftwareOutputChecker.EE.check(af, "[[a1,a2,a0]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEELessThan2Chars() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "f", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEENoOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEENoClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEENoComma() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[a][b]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEEUnexpectedOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[[a]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEEUnexpectedClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[][a]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorEENoFinalClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[a]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorSELessThan2Chars() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "f", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorSENoOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorSENoClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "[[", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testSyntaxErrorSENotALetterOrDigit() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "[#]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@Test
	void testTranslationHistoryInError() {
		final CheckResult noHistory = SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "[[],[b]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult withHistory = SoftwareOutputChecker.EE.check(new ArgumentationFramework(), "[[],[b]]", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		assertTrue(noHistory.getExplanation().length() < withHistory.getExplanation().length());
	}
	
	@Test
	void testCE() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		assertSuccess(SoftwareOutputChecker.CE.check(af, "2", SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "abc", "2.0"})
	void testCEErrors(final String arg) {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		assertNotSuccess(SoftwareOutputChecker.CE.check(af, arg, SolverOutputDecoderFactory.ICCMA17.getDecoderInstance()));
	}
	
	private void assertSuccess(final CheckResult result) {
		assertTrue(result.isSuccessful());
	}
	
	private void assertNotSuccess(final CheckResult result) {
		assertFalse(result.isSuccessful());
	}

}
