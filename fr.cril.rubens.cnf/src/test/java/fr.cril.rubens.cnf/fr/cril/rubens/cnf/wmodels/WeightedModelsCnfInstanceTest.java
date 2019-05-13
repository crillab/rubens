package fr.cril.rubens.cnf.wmodels;

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

public class WeightedModelsCnfInstanceTest {
	
	private CnfInstance decorated;
	
	private WeightedModelsCnfInstance instance;

	@Before
	public void setUp() {
		this.decorated = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		this.instance = new WeightedModelsCnfInstance(decorated);
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
		this.instance = new WeightedModelsCnfInstance();
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	public void testExtensions() {
		assertEquals(Arrays.stream(new String[]{CnfInstance.CNF_EXT, WeightedModelsCnfInstance.W_EXT, WeightedModelsCnfInstance.WMODS_EXT}).collect(Collectors.toSet()),
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
	public void testWModsOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(WeightedModelsCnfInstance.WMODS_EXT, outputStream);
		assertEquals(this.instance.modelWeights().get(Stream.of(1).collect(Collectors.toSet()))+" 1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongOutputExt() throws IOException {
		this.instance.write("toto", null);
	}

}
