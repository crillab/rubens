package fr.cril.rubens.arg.checking.decoders;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.ICCMA17SolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;

public class ICCMA17SolverOutputDecoderTest {
	
	private ICCMA17SolverOutputDecoder decoder;

	@Before
	public void setUp() {
		this.decoder = new ICCMA17SolverOutputDecoder();
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testLessThan3ArgsInD3() throws SyntaxErrorException {
		this.decoder.readD3(""
				+ "[\n"
				+ "[]\n"
				+ "]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitExtSetsOutOfBrackets() throws SyntaxErrorException {
		this.decoder.splitExtensionSets("a\n");
	}
	
	@Test
	public void testSplitExtSetsSingleArg() throws SyntaxErrorException {
		final List<String> list = this.decoder.splitExtensionSets("[[a0]],[[a0]],[[a0]]");
		assertEquals(3, list.size());
		for(final String ext : list) {
			assertEquals("[[a0]]", ext);
		}
	}
	
	@Test
	public void testSplitExtSetsDualArg() throws SyntaxErrorException {
		final List<String> list = this.decoder.splitExtensionSets("[[a0,a1]],[[a0,a1]],[[a0,a1]]");
		assertEquals(3, list.size());
		for(final String ext : list) {
			assertEquals("[[a0,a1]]", ext);
		}
	}
	
	@Test
	public void testDynAcceptanceStatuses() throws SyntaxErrorException {
		final List<String> statuses = this.decoder.splitDynAcceptanceStatuses("YES, NO");
		assertEquals(Stream.of("YES", "NO").collect(Collectors.toList()), statuses);
	}
	
	@Test
	public void testSplitDynExtensions() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensions("[a0],[a1]");
		assertEquals(Stream.of("[a0]", "[a1]").collect(Collectors.toList()), extensions);
	}
	
	@Test
	public void testSplitDynExtensionSets() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensionSets("[[a0]],[[a1]]");
		assertEquals(Stream.of("[[a0]]", "[[a1]]").collect(Collectors.toList()), extensions);
	}

}
