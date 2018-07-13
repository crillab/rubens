package fr.cril.rubens.cnf.mc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;

public class ModelCountingCnfInstanceTest {
	
	private CnfInstance decorated;
	
	private ModelCountingCnfInstance instance;

	@Before
	public void setUp() {
		this.decorated = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		this.instance = new ModelCountingCnfInstance(decorated);
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
		this.instance = new ModelCountingCnfInstance();
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	public void testExtensions() {
		assertEquals(Arrays.stream(new String[]{CnfInstance.CNF_EXT, ModelCountingCnfInstance.MC_EXT}).collect(Collectors.toSet()),
				new HashSet<>(this.instance.getFileExtensions()));
	}
	
	@Test
	public void testCnfOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(CnfInstance.CNF_EXT, outputStream);
		assertEquals("p cnf 1 1\n1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	public void testMCOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(ModelCountingCnfInstance.MC_EXT, outputStream);
		assertEquals("1", new String(outputStream.toByteArray()).trim());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongOutputExt() throws IOException {
		this.instance.write("toto", null);
	}

}
