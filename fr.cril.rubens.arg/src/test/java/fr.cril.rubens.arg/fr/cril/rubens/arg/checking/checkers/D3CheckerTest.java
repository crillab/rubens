package fr.cril.rubens.arg.checking.checkers;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;

public class D3CheckerTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}
	
	@Test
	public void testCheckSoftwareOutputEmptyGR() {
		final Argument arg0 = Argument.getInstance("a0");
		final Argument arg1 = Argument.getInstance("a1");
		final Attack att00 = Attack.getInstance(arg0, arg0);
		final Attack att01 = Attack.getInstance(arg0, arg1);
		final ArgumentationFramework af = new ArgumentationFramework(Stream.of(arg0, arg1).collect(ArgumentSet.collector()), Stream.of(att00, att01).collect(AttackSet.collector()), ExtensionSet.getInstance(Collections.emptySet()));
		final D3ArgumentationFramework d3af = new D3ArgumentationFramework(af);
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[[]], [], [[]]");
		assertTrue(result.isSuccessful());
	}

}
