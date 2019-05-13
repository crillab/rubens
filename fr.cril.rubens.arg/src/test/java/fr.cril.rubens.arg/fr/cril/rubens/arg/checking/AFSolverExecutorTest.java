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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.arg.core.AArgumentationFrameworkGraph;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;

public class AFSolverExecutorTest {
	
	@Test
	public void testNoDynNoArg() {
		final TestingExecutor executor = new TestingExecutor("SE-CO", false);
		final List<String> args = executor.getCliArgs();
		assertFalse(args.contains("-m"));
		assertFalse(args.contains("-a"));
	}
	
	@Test
	public void testNoDynArg() {
		final TestingExecutor executor = new TestingExecutor("DC-CO", false);
		final List<String> args = executor.getCliArgs();
		assertFalse(args.contains("-m"));
		assertTrue(args.contains("-a"));
	}
	
	@Test
	public void testDynNoArg() {
		final TestingExecutor executor = new TestingExecutor("SE-CO-D", true);
		final List<String> args = executor.getCliArgs();
		assertTrue(args.contains("-m"));
		assertFalse(args.contains("-a"));
	}
	
	@Test
	public void testDynArg() {
		final TestingExecutor executor = new TestingExecutor("DC-CO-D", true);
		final List<String> args = executor.getCliArgs();
		assertTrue(args.contains("-m"));
		assertTrue(args.contains("-a"));
	}
	
	public class TestingExecutor extends AFSolverExecutor<AArgumentationFrameworkGraph> {
		
		private Map<String, Path> instances = new HashMap<>();

		public TestingExecutor(final String problem, final boolean hasDyn) {
			super(Paths.get("/bin/foo"), problem);
			instances.put(ArgumentationFramework.APX_EXT, Paths.get("/instance.apx"));
			if(hasDyn) {
				instances.put(DynamicArgumentationFramework.APXM_EXT, Paths.get("/instance.apxm"));
			}
		}
		
		public List<String> getCliArgs() {
			Argument arg = Argument.getInstance("a");
			ArgumentSet argSet = Stream.of(arg).collect(ArgumentSet.collector());
			final ArgumentationFramework instance = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.emptySet()), Stream.of(argSet).collect(ExtensionSet.collector()));
			instance.setArgUnderDecision(arg);
			return cliArgs(Paths.get("/bin/foo"), this.instances, instance);
		}
		
	}

}
