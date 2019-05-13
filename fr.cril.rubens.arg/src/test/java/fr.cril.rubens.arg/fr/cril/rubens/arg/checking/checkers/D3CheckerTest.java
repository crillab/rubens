package fr.cril.rubens.arg.checking.checkers;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.AFSolverExecutor;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.D3TestGeneratorFactory;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;

public class D3CheckerTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}
	
	@Test
	public void testCheckSoftwareOutputEmptyGR() {
		final D3ArgumentationFramework d3af = emptyGrAF();
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[[]], [], [[]]");
		assertTrue(result.isSuccessful());
	}

	private D3ArgumentationFramework emptyGrAF() {
		final Argument arg0 = Argument.getInstance("a0");
		final Argument arg1 = Argument.getInstance("a1");
		final Attack att00 = Attack.getInstance(arg0, arg0);
		final Attack att01 = Attack.getInstance(arg0, arg1);
		final ArgumentationFramework af = new ArgumentationFramework(Stream.of(arg0, arg1).collect(ArgumentSet.collector()), Stream.of(att00, att01).collect(AttackSet.collector()), ExtensionSet.getInstance(Collections.emptySet()));
		final D3ArgumentationFramework d3af = new D3ArgumentationFramework(af);
		return d3af;
	}
	
	@Test
	public void testNewTestGenerator() {
		assertTrue(new D3Checker().newTestGenerator() instanceof D3TestGeneratorFactory);
	}
	
	@Test
	public void testNewExecutor() {
		assertTrue(new D3Checker().newExecutor(Paths.get("/foo/bar")) instanceof AFSolverExecutor);
	}
	
	@Test
	public void testSyntaxError() {
		final D3ArgumentationFramework d3af = emptyGrAF();
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[]], [], [[]]");
		assertFalse(result.isSuccessful());
	}
	
	@Test
	public void testGrError() {
		final D3ArgumentationFramework d3af = emptyGrAF();
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[[a0]], [], [[]]");
		assertFalse(result.isSuccessful());
	}
	
	@Test
	public void testStError() {
		final D3ArgumentationFramework d3af = emptyGrAF();
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[[]], [[a0]], [[]]");
		assertFalse(result.isSuccessful());
	}
	
	@Test
	public void testPrError() {
		final D3ArgumentationFramework d3af = emptyGrAF();
		final D3Checker checker = new D3Checker();
		checker.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final CheckResult result = checker.checkSoftwareOutput(d3af, "[[]], [], [[a0]]");
		assertFalse(result.isSuccessful());
	}
	
	@Test
	public void testOptions() {
		assertEquals(1, new D3Checker().getOptions().size());
	}

}
