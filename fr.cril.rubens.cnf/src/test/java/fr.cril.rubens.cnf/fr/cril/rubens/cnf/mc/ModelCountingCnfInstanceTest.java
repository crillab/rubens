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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.cnf.core.CnfInstance;

class ModelCountingCnfInstanceTest {
	
	private CnfInstance decorated;
	
	private ModelCountingCnfInstance instance;

	@BeforeEach
	public void setUp() {
		this.decorated = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		this.instance = new ModelCountingCnfInstance(decorated);
	}
	
	@Test
	void testInheritance() {
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	void testInheritanceOfEmpty() {
		this.decorated = new CnfInstance();
		this.instance = new ModelCountingCnfInstance();
		assertEquals(this.decorated.nVars(), this.instance.nVars());
		assertEquals(this.decorated.clauses(), this.instance.clauses());
		assertEquals(this.decorated.models(), this.instance.models());
	}
	
	@Test
	void testExtensions() {
		assertEquals(Arrays.stream(new String[]{CnfInstance.CNF_EXT, ModelCountingCnfInstance.MC_EXT}).collect(Collectors.toSet()),
				new HashSet<>(this.instance.getFileExtensions()));
	}
	
	@Test
	void testCnfOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(CnfInstance.CNF_EXT, outputStream);
		assertEquals("p cnf 1 1\n1 0", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	void testMCOutput() throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		this.instance.write(ModelCountingCnfInstance.MC_EXT, outputStream);
		assertEquals("1", new String(outputStream.toByteArray()).trim());
	}
	
	@Test
	void testWrongOutputExt() throws IOException {
		assertThrows(IllegalArgumentException.class, () -> this.instance.write("toto", null));
	}

}
