package fr.cril.rubens.cnf.wmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.wmodels.WeightedModelsCnfInstance;

public class WeightedModelCountingCnfInstanceTest {
	
	private CnfInstance decorated;
	
	private WeightedModelCountingCnfInstance instance;

	@Before
	public void setUp() {
		this.decorated = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		this.instance = new WeightedModelCountingCnfInstance(decorated);
	}
	
	@Test
	public void testInheritance() {
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	public void testInheritanceOfEmpty() {
		this.decorated = new CnfInstance();
		this.instance = new WeightedModelCountingCnfInstance();
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	public void testExtensions() {
		assertEquals(Arrays.stream(new String[]{CnfInstance.CNF_EXT, WeightedModelCountingCnfInstance.W_EXT, WeightedModelCountingCnfInstance.WMC_EXT}).collect(Collectors.toSet()),
				new HashSet<>(this.instance.getFileExtensions()));
	}
	
	@Test
	public void testCnfOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(CnfInstance.CNF_EXT, outputStream);
		assertEquals("p cnf 1 1\n1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	public void testWOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(WeightedModelsCnfInstance.W_EXT, outputStream);
		final Map<Integer, Integer> weights = Arrays.stream(new String(outputStream.toByteArray()).trim().split("\n")).filter(Objects::nonNull).filter(s -> !s.isEmpty())
				.collect(Collectors.toMap(k -> Integer.valueOf(k.split(" ")[0]), k -> Integer.valueOf(k.split(" ")[1])));
		assertFalse(weights.containsValue(null));
		assertFalse(weights.containsValue(1));
		assertFalse(weights.size() > (this.instance.nVars() << 1));
	}
	
	@Test
	public void testMCOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(WeightedModelCountingCnfInstance.WMC_EXT, outputStream);
		assertEquals(this.instance.modelWeights().get(Stream.of(1).collect(Collectors.toSet())).toString(), new String(outputStream.toByteArray()).trim());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongOutputExt() throws IOException {
		this.instance.write("toto", null);
	}

}
