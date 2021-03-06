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

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CnfInstanceTest {
	
	private CnfInstance instance;

	@BeforeEach
	public void setUp() {
		this.instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
	}
	
	@Test
	void testEmptyInstance() {
		final CnfInstance cnfInstance = new CnfInstance();
		assertEquals(0, cnfInstance.nVars());
		assertTrue(cnfInstance.clauses().isEmpty());
		final List<List<Integer>> models = cnfInstance.models();
		assertEquals(1, models.size());
		assertTrue(models.iterator().next().isEmpty());
	}
	
	@Test
	void testNVars() {
		assertEquals(1, this.instance.nVars());
	}
	
	@Test
	void testClauses() {
		final List<List<Integer>> instanceClauses = this.instance.clauses();
		assertEquals(1, instanceClauses.size());
		assertEquals(Stream.of(1).collect(Collectors.toList()), instanceClauses.get(0));
	}
	
	@Test
	void testModels() {
		final List<List<Integer>> instanceModels = this.instance.models();
		assertEquals(1, instanceModels.size());
		assertEquals(Stream.of(1).collect(Collectors.toList()), instanceModels.iterator().next());
	}
	
	@Test
	void testExtensions() {
		final List<String> extensions = new ArrayList<>(this.instance.getFileExtensions());
		assertEquals(2, extensions.size());
		assertTrue(extensions.contains(CnfInstance.CNF_EXT));
		assertTrue(extensions.contains(CnfInstance.MODS_EXT));
	}
	
	@Test
	void testCnfOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(CnfInstance.CNF_EXT, outputStream);
		assertEquals("p cnf 1 1\n1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	void testModelsOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(CnfInstance.MODS_EXT, outputStream);
		assertEquals("1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	void testWrongOutputExt() {
		assertThrows(IllegalArgumentException.class, () -> this.instance.write("toto", null));
	}
	
	@Test
	void testCopyConstructor() {
		final CnfInstance copy = new CnfInstance(this.instance);
		assertEquals(this.instance.nVars(), copy.nVars());
		assertEquals(this.instance.clauses(), copy.clauses());
		assertEquals(this.instance.models(), copy.models());
	}
	
	@Test
	void testHashcodeAndEquals() {
		EqualsVerifier.forClass(CnfInstance.class).verify();
	}
	
	@Test
	void testToString() {
		assertEquals("[nVars=1, clauses=[[1]], models=[[1]]]", this.instance.toString());
	}

}
