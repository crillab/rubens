package fr.cril.rubens.arg.checking.decoders;

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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ICCMA17SolverOutputDecoderTest {
	
	private ICCMA17SolverOutputDecoder decoder;

	@BeforeEach
	public void setUp() {
		this.decoder = new ICCMA17SolverOutputDecoder();
	}
	
	@Test
	void testLessThan3ArgsInD3() {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.readD3(""
				+ "[\n"
				+ "[]\n"
				+ "]\n"));
	}
	
	@Test
	void testSplitExtSetsOutOfBrackets() {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.splitExtensionSets("a\n"));
	}
	
	@Test
	void testSplitExtSetsSingleArg() throws SyntaxErrorException {
		final List<String> list = this.decoder.splitExtensionSets("[[a0]],[[a0]],[[a0]]");
		assertEquals(3, list.size());
		for(final String ext : list) {
			assertEquals("[[a0]]", ext);
		}
	}
	
	@Test
	void testSplitExtSetsDualArg() throws SyntaxErrorException {
		final List<String> list = this.decoder.splitExtensionSets("[[a0,a1]],[[a0,a1]],[[a0,a1]]");
		assertEquals(3, list.size());
		for(final String ext : list) {
			assertEquals("[[a0,a1]]", ext);
		}
	}
	
	@Test
	void testDynAcceptanceStatuses() throws SyntaxErrorException {
		final List<String> statuses = this.decoder.splitDynAcceptanceStatuses("YES, NO");
		assertEquals(Stream.of("YES", "NO").collect(Collectors.toList()), statuses);
	}
	
	@Test
	void testSplitDynExtensions() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensions("[a0],[a1]");
		assertEquals(Stream.of("[a0]", "[a1]").collect(Collectors.toList()), extensions);
	}
	
	@Test
	void testSplitDynExtensionSets() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensionSets("[[a0]],[[a1]]");
		assertEquals(Stream.of("[[a0]]", "[[a1]]").collect(Collectors.toList()), extensions);
	}
	
	@Test
	void testReadExtensionCountOk() throws SyntaxErrorException {
		assertEquals(1, this.decoder.readExtensionCount("  1 "));
	}
	
	@Test
	void testReadExtensionCountNotOk() {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.readExtensionCount("a"));
	}

}
