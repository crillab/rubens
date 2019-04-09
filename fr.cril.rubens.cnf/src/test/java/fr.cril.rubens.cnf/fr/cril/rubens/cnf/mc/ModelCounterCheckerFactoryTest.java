package fr.cril.rubens.cnf.mc;

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

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.core.CheckResult;

public class ModelCounterCheckerFactoryTest {
	
	private ModelCounterCheckerFactory factory;

	@Before
	public void setUp() {
		this.factory = new ModelCounterCheckerFactory();
	}
	
	@Test
	public void testSetOptions() {
		this.factory.setOptions("nevermind");
	}
	
	@Test
	public void testNewGenerator() {
		assertTrue(this.factory.newTestGenerator() instanceof ModelCountingCnfTestGeneratorFactory);
	}
	
	@Test
	public void testNewExecutor() {
		final Path path = Paths.get("foo", "bar");
		assertTrue(this.factory.newExecutor(path) instanceof CnfSolverExecutor);
	}
	
	@Test
	public void testUnsat() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet()));
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 0\n"));
	}
	
	@Test
	public void testSat() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1))));
		assertSuccess(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 1\n"));
	}
	
	@Test
	public void testWrongCount() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1))));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\ns 0\n"));
	}
	
	@Test
	public void testMultipleStatusLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns 1\ns 1\n"));
	}
	
	@Test
	public void testNoStatusLine() {
		final ModelCountingCnfInstance instance = new ModelCountingCnfInstance(new CnfInstance(1,
				Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet()));
		assertError(this.factory.checkSoftwareOutput(instance, "c this is a comment\n"));
	}
	
	@Test
	public void testUnexpectedLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\nthis is unexpected\ns UNSATISFIABLE\n"));
	}
	
	@Test
	public void testUnexpectedStatusLine() {
		assertError(this.factory.checkSoftwareOutput(new ModelCountingCnfInstance(), "c this is a comment\ns FOO\n"));
	}
	
	private void assertSuccess(final CheckResult result) {
		assertEquals(CheckResult.SUCCESS, result);
	}
	
	private void assertError(final CheckResult result) {
		assertNotEquals(CheckResult.SUCCESS, result);
	}

}
