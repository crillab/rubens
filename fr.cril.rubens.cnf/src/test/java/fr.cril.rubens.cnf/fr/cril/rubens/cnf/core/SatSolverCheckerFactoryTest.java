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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.cril.rubens.core.CheckResult;

class SatSolverCheckerFactoryTest {
	
	private SatSolverCheckerFactory factory;

	@BeforeEach
	public void setUp() {
		this.factory = new SatSolverCheckerFactory();
	}
	
	@Test
	void testNewGenerator() {
		assertTrue(this.factory.newTestGenerator() instanceof CnfTestGeneratorFactory);
	}
	
	@Test
	void testNewExecutor() {
		final Path path = Paths.get("foo", "bar");
		assertTrue(this.factory.newExecutor(path) instanceof CnfSolverExecutor);
	}
	
	@Test
	void testUnsat() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptyList());
		assertEquals(CheckResult.SUCCESS, this.factory.checkSoftwareOutput(instance, "c this is a comment\ns UNSATISFIABLE\n"));
	}
	
	@Test
	void testSat() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertEquals(CheckResult.SUCCESS, this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv 1 0\n"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"c this is a comment\ns UNSATISFIABLE\ns UNSATISFIABLE\n",
			"c this is a comment\n",
			"c this is a comment\nv 1 0\nSATISFIABLE\n",
			"c this is a comment\nthis is unexpected\ns UNSATISFIABLE\n",
			"c this is a comment\ns\n",
			"c this is a comment\ns1\n",
			"c this is a comment\ns FOO\n",
			"c this is a comment\ns SATISFIABLE\n",
			"c this is a comment\ns SATISFIABLE\nv -1 0\n",
			"c this is a comment\ns SATISFIABLE\nv 1\n",
			"c this is a comment\ns SATISFIABLE\nv 0 1\n",
			"c this is a comment\ns SATISFIABLE\nv 0\nv 1 0\n",
			"c this is a comment\ns SATISFIABLE\nv\nv 1\n",
			"c this is a comment\ns SATISFIABLE\nvv\nv 1\n",
			"c this is a comment\ns SATISFIABLE\nv v 1\n",
			"c this is a comment\ns UNSATISFIABLE\nv -1 0\n",
			"c this is a comment\ns UNSATISFIABLE\n"
	})
	void testIncorrectOutput(final String arg) {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertNotEquals(CheckResult.SUCCESS, this.factory.checkSoftwareOutput(instance, arg));
	}

}
