package fr.cril.rubens.arg.checking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;

public class SoftwareOutputCheckerTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}
	
	private ArgumentationFramework toAF(final String[]... argSets) {
		final ExtensionSet exts = Arrays.stream(argSets).map(set -> Arrays.stream(set).map(Argument::getInstance).collect(ArgumentSet.collector())).collect(ExtensionSet.collector());
		final ArgumentSet arguments = ArgumentSet.getInstance(Stream.of("a", "b", "c").map(Argument::getInstance).collect(Collectors.toSet()));
		return new ArgumentationFramework(arguments, AttackSet.getInstance(Collections.emptySet()), exts);
	}
	
	
	@Test
	public void testEmptyEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(), "[]"));
	}
	
	@Test
	public void testSingleEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}), "[[]]"));
	}
	
	@Test
	public void testDualEE() {
		assertSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "[[],[a]]"));
	}
	
	@Test
	public void testFalseEE() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "[[],[b]]"));
	}
	
	@Test
	public void testWrongValEE() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(new String[]{}, new String[]{"a"}), "foo"));
	}
	
	@Test
	public void testEmptySE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(), "NO"));
	}
	
	@Test
	public void testNoOnNonEmptySE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "NO"));
	}
	
	@Test
	public void testSingleArgSE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "[a]"));
	}
	
	@Test
	public void testDualArgSE() {
		assertSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a", "b"}), "[a,b]"));
	}
	
	@Test
	public void testFalseSE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a"}), "[b]"));
	}
	
	@Test
	public void testWrongValSE() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(new String[]{}, new String[]{"a", "b"}), "foo"));
	}
	
	@Test
	public void testYesDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertSuccess(SoftwareOutputChecker.DC.check(af, "YES"));
	}
	
	@Test
	public void testNoDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("c"));
		assertSuccess(SoftwareOutputChecker.DC.check(af, "NO"));
	}
	
	@Test
	public void testFalseYesDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("c"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "YES"));
	}
	
	@Test
	public void testFalseNoDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "NO"));
	}
	
	@Test
	public void testWrongValDC() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "foo"));
		af.setArgUnderDecision(Argument.getInstance("c"));
		assertNotSuccess(SoftwareOutputChecker.DC.check(af, "foo"));
	}
	
	@Test
	public void testYesDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertSuccess(SoftwareOutputChecker.DS.check(af, "YES"));
	}
	
	@Test
	public void testNoDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertSuccess(SoftwareOutputChecker.DS.check(af, "NO"));
	}
	
	@Test
	public void testFalseYesDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "YES"));
	}
	
	@Test
	public void testFalseNoDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "NO"));
	}
	
	@Test
	public void testWrongValDS() {
		final ArgumentationFramework af = toAF(new String[]{"a"}, new String[]{"a", "b"});
		af.setArgUnderDecision(Argument.getInstance("a"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "foo"));
		af.setArgUnderDecision(Argument.getInstance("b"));
		assertNotSuccess(SoftwareOutputChecker.DS.check(af, "foo"));
	}
	
	@Test
	public void testSE3args() {
		final ArgumentationFramework af = toAF(new String[]{"a0", "a1", "a2"});
		assertSuccess(SoftwareOutputChecker.SE.check(af, "[a1,a2,a0]"));
	}
	
	@Test
	public void testEE1ext3args() {
		final ArgumentationFramework af = toAF(new String[]{"a0", "a1", "a2"});
		assertSuccess(SoftwareOutputChecker.EE.check(af, "[[a1,a2,a0]]"));
	}
	
	@Test
	public void testSyntaxErrorEELessThan2Chars() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "f"));
	}
	
	@Test
	public void testSyntaxErrorEENoOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "]]"));
	}
	
	@Test
	public void testSyntaxErrorEENoClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[["));
	}
	
	@Test
	public void testSyntaxErrorEENoComma() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[a][b]]"));
	}
	
	@Test
	public void testSyntaxErrorEEUnexpectedOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[[a]]"));
	}
	
	@Test
	public void testSyntaxErrorEEUnexpectedClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[][a]]"));
	}
	
	@Test
	public void testSyntaxErrorEENoFinalClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.EE.check(toAF(), "[[a]"));
	}
	
	@Test
	public void testSyntaxErrorSELessThan2Chars() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "f"));
	}
	
	@Test
	public void testSyntaxErrorSENoOpeningBracket() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "]]"));
	}
	
	@Test
	public void testSyntaxErrorSENoClosingBracket() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "[["));
	}
	
	@Test
	public void testSyntaxErrorSENotALetterOrDigit() {
		assertNotSuccess(SoftwareOutputChecker.SE.check(toAF(), "[#]"));
	}
	
	private void assertSuccess(final CheckResult result) {
		assertTrue(result.isSuccessful());
	}
	
	private void assertNotSuccess(final CheckResult result) {
		assertFalse(result.isSuccessful());
	}

}
