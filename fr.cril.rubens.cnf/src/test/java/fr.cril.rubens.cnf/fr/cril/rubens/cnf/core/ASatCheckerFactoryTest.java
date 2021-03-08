package fr.cril.rubens.cnf.core;

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

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

class ASatCheckerFactoryTest {
	
	@Test
	void testIgnoreUnsat() {
		final LocalFactory factory = new LocalFactory();
		factory.getOptions().stream().filter(o -> o.getName().equals("ignoreUnsat")).findFirst().orElseThrow().apply("on");
		assertTrue(factory.getIgnUnsat());
	}
	
	@Test
	void testMultipleIgn() {
		final LocalFactory factory = new LocalFactory();
		assertFalse(factory.getIgnUnsat());
		factory.ignoreUnsat(true);
		assertTrue(factory.getIgnUnsat());
		factory.ignoreUnsat(true);
		assertTrue(factory.getIgnUnsat());
		factory.ignoreUnsat(false);
		assertFalse(factory.getIgnUnsat());
		factory.ignoreUnsat(false);
		assertFalse(factory.getIgnUnsat());
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
			return ignoreInstance(new CnfInstance(1, Stream.of(Collections.singletonList(1), Collections.singletonList(-1)).collect(Collectors.toList()), Collections.emptyList()));
		}
		
	}

}
