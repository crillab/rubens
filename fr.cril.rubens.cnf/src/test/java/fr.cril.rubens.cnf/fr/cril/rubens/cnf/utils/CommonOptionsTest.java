package fr.cril.rubens.cnf.utils;

/*-
 * #%L
 * RUBENS module for CNF handling
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

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.cnf.core.ASatCheckerFactory;
import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class CommonOptionsTest {
	
	@Test
	public void testSingleton() {
		assertTrue(CommonOptions.getInstance() == CommonOptions.getInstance());
	}
	
	@Test
	public void testGetOptions() {
		assertEquals(1, CommonOptions.getInstance().getOptions(new LocalFactory()).size());
	}
	
	@Test
	public void testIgnUnsatTrue() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("on");
		assertTrue(factory.getIgnUnsat());
	}
	
	@Test
	public void testIgnUnsatFalse() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("off");
		assertFalse(factory.getIgnUnsat());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIgnUnsatWrongVal() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("foo");
	}
	
	private class LocalFactory extends ASatCheckerFactory<CnfInstance> {
		
		@Override
		public TestGeneratorFactory<CnfInstance> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<CnfInstance> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(CnfInstance instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}
		
		private boolean getIgnUnsat() {
			return ignoreInstance(new CnfInstance(1, Stream.of(Collections.singletonList(1), Collections.singletonList(-1)).collect(Collectors.toList()), Collections.emptySet()));
		}
		
	}

}
