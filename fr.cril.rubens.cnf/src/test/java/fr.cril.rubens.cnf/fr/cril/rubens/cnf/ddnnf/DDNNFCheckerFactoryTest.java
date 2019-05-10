package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.cnf.core.CnfTestGeneratorFactory;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;

public class DDNNFCheckerFactoryTest {
	
	private DDNNFCheckerFactory factory;
	
	@Before
	public void setUp() {
		this.factory = new DDNNFCheckerFactory();
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
	public void testOk() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 1 0 1\nL 1"));
	}
	
	@Test
	public void testReadError() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertFalse(factory.checkSoftwareOutput(instance, "nnf 0 0 1\nL 1").isSuccessful());
	}
	
	@Test
	public void testWrongDDNNFModel() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertFalse(factory.checkSoftwareOutput(instance, "nnf 1 0 1\nA 0").isSuccessful());
	}
	
	@Test
	public void testMissingDDNNFModel() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertFalse(factory.checkSoftwareOutput(instance, "nnf 1 0 1\nO 0 0").isSuccessful());
	}
	
	@Test
	public void testNoConstraints() {
		final CnfInstance instance = new CnfInstance(2, Collections.emptyList(), Stream.of(
				Stream.of(-1, -2).collect(Collectors.toSet()),
				Stream.of(1, -2).collect(Collectors.toSet()),
				Stream.of(-1, 2).collect(Collectors.toSet()),
				Stream.of(1, 2).collect(Collectors.toSet())
		).collect(Collectors.toSet()));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 2 0 2\nO 0 0\nA 0\n"));
	}
	
	@Test
	public void testOptions() {
		final List<MethodOption> opts = this.factory.getOptions();
		assertEquals(2, opts.size());
		assertTrue(opts.stream().anyMatch(o -> o.getName().equals("ignoreUnsat")));
		assertTrue(opts.stream().anyMatch(o -> o.getName().equals("ignorePreamble")));
	}
	
	@Test
	public void testDoNotIgnPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		opt.apply("off");
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertFalse(factory.checkSoftwareOutput(instance, "nnf 0 0 1\nL 1").isSuccessful());
	}
	
	@Test
	public void testIgnPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		opt.apply("on");
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singleton(Collections.singleton(1)));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 0 0 1\nL 1"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		opt.apply("foo");
	}
	
}
