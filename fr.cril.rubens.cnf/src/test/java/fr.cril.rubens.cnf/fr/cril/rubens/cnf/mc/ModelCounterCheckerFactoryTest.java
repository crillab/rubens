package fr.cril.rubens.cnf.mc;

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

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.core.CheckResult;

class ModelCounterCheckerFactoryTest {
	
	private ModelCounterCheckerFactory factory;

	@BeforeEach
	public void setUp() {
		this.factory = new ModelCounterCheckerFactory();
	}
	
	@Test
	void testNewGenerator() {
		assertTrue(this.factory.newTestGenerator() instanceof ModelCountingCnfTestGeneratorFactory);
	}
	
	@Test
	void testNewExecutor() {
		final Path path = Paths.get("foo", "bar");
		assertTrue(this.factory.newExecutor(path) instanceof CnfSolverExecutor);
	}
	
	@Test
	void testUnsat() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptyList()));
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 0\n"));
	}
	
	@Test
	void testSat() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1))));
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 1\n"));
	}
	
	@Test
	void testWrongCount() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1))));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 0\n"));
	}
	
	@Test
	void testMultipleStatusLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns 1\ns 1\n"));
	}
	
	@Test
	void testNoStatusLine() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptyList()));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\n"));
	}
	
	@Test
	void testUnexpectedLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\nthis is unexpected\ns UNSATISFIABLE\n"));
	}
	
	@Test
	void testUnexpectedStatusLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns FOO\n"));
	}
	
	@Test
	void testStatusLineOfLength1() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns\n"));
	}
	
	@Test
	void testStatusLineOfNoSpace() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns123\n"));
	}
	
	private void assertSuccess(final CheckResult result) {
		assertEquals(CheckResult.SUCCESS, result);
	}
	
	private void assertError(final CheckResult result) {
		assertNotEquals(CheckResult.SUCCESS, result);
	}

}
