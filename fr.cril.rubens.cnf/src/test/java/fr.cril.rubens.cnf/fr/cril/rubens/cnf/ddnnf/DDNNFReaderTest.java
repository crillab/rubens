package fr.cril.rubens.cnf.ddnnf;

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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DDNNFReaderTest {
	
	@Test
	void testOK() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	void testTrue() throws DDNNFException {
		final String instance = "nnf 1 0 1\n"
				+ "A 0\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	void testFalse() throws DDNNFException {
		final String instance = "nnf 1 0 1\n"
				+ "O 0 0";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(0, Utils.countModels(ddnnf));
	}
	
	@Test
	void testFreeVars() throws DDNNFException {
		final String instance = "nnf 7 6 4\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(8, Utils.countModels(ddnnf));
	}
	
	@Test
	void testNoConstraints() throws DDNNFException {
		final String instance = "nnf 0 0 1\n"
				+ "\n"
				+ "\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"nnf 7 6 1\n", "nnf 6 6 2\n", "nnf 7 7 2\n", "", "nnf 0 0\n", "dnnf 0 0 1\n", "nnf 0 a 1\n", "nnf a 0 1\n", "nnf 0 0 a\n"})
	void testErrorInPreamble(final String arg) throws DDNNFException {
		final String instance = arg
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"nnf 7 6 1\n", "nnf 6 6 2\n", "nnf 7 7 2\n"})
	void testIgnErrorInPreamble(final String arg) throws DDNNFException {
		final String instance = arg
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader(true);
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	void testEmptyLine() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testUnknownNode() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "AND 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testNoChildrenCountInAnd() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testWrongChildrenCountInAnd() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 3 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testNoChildrenCountInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testWrongChildrenCountInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 3 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testIncompatibleParamsInOr1() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 0\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testIncompatibleParamsInOr2() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 0 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testTooMuchChildrenInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 3 2 5 0\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testNegativeChildrenCount() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A -1 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5 \n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testUnknownCflVarInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 3 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testNoLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testZeroLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L 0\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testWrongLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L 3\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testAlphaLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L a\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}
	
	@Test
	void testRefToFurtherDeclaredNode() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 3 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		assertThrows(DDNNFException.class, () -> reader.read(instance));
	}

}
