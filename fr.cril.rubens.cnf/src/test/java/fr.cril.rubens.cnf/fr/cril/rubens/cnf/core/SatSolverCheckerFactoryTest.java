package fr.cril.rubens.cnf.core;

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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.core.CheckResult;

public class SatSolverCheckerFactoryTest {
	
	private SatSolverCheckerFactory factory;

	@Before
	public void setUp() {
		this.factory = new SatSolverCheckerFactory();
	}
	
	@Test
	public void testNewGenerator() {
		assertTrue(this.factory.newTestGenerator() instanceof CnfTestGeneratorFactory);
	}
	
	@Test
	public void testNewExecutor() {
		final Path path = Paths.get("foo", "bar");
		assertTrue(this.factory.newExecutor(path) instanceof CnfSolverExecutor);
	}
	
	@Test
	public void testUnsat() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet());
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns UNSATISFIABLE\n"));
	}
	
	@Test
	public void testSat() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv 1 0\n"));
	}
	
	@Test
	public void testMultipleStatusLine() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\ns UNSATISFIABLE\ns UNSATISFIABLE\n"));
	}
	
	@Test
	public void testNoStatusLine() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet());
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\n"));
	}
	
	@Test
	public void testMisplacedStatusLine() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\nv 1 0\nSATISFIABLE\n"));
	}
	
	@Test
	public void testUnexpectedLine() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\nthis is unexpected\ns UNSATISFIABLE\n"));
	}
	
	@Test
	public void testUnexpectedStatusLine0() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\ns\n"));
	}
	
	@Test
	public void testUnexpectedStatusLine1() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\ns1\n"));
	}
	
	@Test
	public void testUnexpectedStatusLine2() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\ns FOO\n"));
	}
	
	@Test
	public void testSatButNoValues() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\n"));
	}
	
	@Test
	public void testWrongModel() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv -1 0\n"));
	}
	
	@Test
	public void testNoFinalZero() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv 1\n"));
	}
	
	@Test
	public void testZeroInTheMiddle() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv 0 1\n"));
	}
	
	@Test
	public void testZeroIsNotFinal() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv 0\nv 1 0\n"));
	}
	
	@Test
	public void testSingleV() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv\nv 1\n"));
	}
	
	@Test
	public void testVNotFollowedBySpace() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nvv\nv 1\n"));
	}
	
	@Test
	public void testWrongLiteral() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns SATISFIABLE\nv v 1\n"));
	}
	
	@Test
	public void testUnsatButValues() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet());
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns UNSATISFIABLE\nv -1 0\n"));
	}
	
	@Test
	public void testWrongUnsat() {
		assertError(this.factory.checkSoftwareOutput(new CnfInstance(), "c this is a comment\ns UNSATISFIABLE\n"));
	}

	private void assertSuccess(final CheckResult result) {
		assertEquals(CheckResult.SUCCESS, result);
	}
	
	private void assertError(final CheckResult result) {
		assertNotEquals(CheckResult.SUCCESS, result);
	}

}
