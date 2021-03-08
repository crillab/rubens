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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;

class ICCMA19SolverOutputDecoderTest {
	
	private ICCMA19SolverOutputDecoder decoder;

	@BeforeEach
	public void setUp() {
		Forget.all();
		this.decoder = new ICCMA19SolverOutputDecoder();
	}
	
	@Test
	void testEmptyExtensionSet() throws SyntaxErrorException {
		final ExtensionSet actual = this.decoder.readExtensionSet(""
				+ "[\n"
				+ "  []\n"
				+ "]\n");
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), actual);
	}
	
	@Test
	void testAcceptanceTrue() {
		assertTrue(this.decoder.isAcceptanceTrue("YES\n"));
	}
	
	@Test
	void testD3AllEmpty() throws SyntaxErrorException {
		final List<ExtensionSet> actual = this.decoder.readD3(""
				+ "[\n"
				+ "  []\n"
				+ ""
				+ "]\n"
				+ "[]\n"
				+ "[\n"
				+ "  []\n"
				+ "]\n");
		final ExtensionSet singleEmptyExtSet = ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet())));
		final ExtensionSet emptyExtSet = ExtensionSet.getInstance(Collections.emptySet());
		assertEquals(Stream.of(singleEmptyExtSet, emptyExtSet, singleEmptyExtSet).collect(Collectors.toList()), actual);
	}
	
	@Test
	void testD3SingleArg() throws SyntaxErrorException {
		final List<ExtensionSet> actual = this.decoder.readD3(""
				+ "[\n"
				+ "  [a0]\n"
				+ "]\n"
				+ "[\n"
				+ "  [a0]\n"
				+ "]\n"
				+ "[\n"
				+ "  [a0]\n"
				+ "]\n");
		final Argument arg = Argument.getInstance("a0");
		final ExtensionSet extSet = ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(arg))));
		assertEquals(Stream.of(extSet, extSet, extSet).collect(Collectors.toList()), actual);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"[[a0]]", "]\n]\n", "[\n[\n"})
	void testReadStrExtListErr(final String arg) {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.readStrExtensionList(arg));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"[\n[\n[a0]\n]\n]\n]\n", "[\n[\n[a0]\n]\n", "[\n[\n[a0]\n]\n]\na1\n"})
	void testSplitExtSetErr(final String arg) {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.splitExtensionSets(arg));
	}
	
	@Test
	void testNormalizeresult() {
		assertEquals("", this.decoder.normalizeResult(""));
	}
	
	@Test
	void testDynAcceptanceStatuses() throws SyntaxErrorException {
		final List<String> statuses = this.decoder.splitDynAcceptanceStatuses("[YES, NO]");
		assertEquals(Stream.of("YES", "NO").collect(Collectors.toList()), statuses);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"[", "[[", "]]"})
	void testDynAcceptanceStatusesErr(final String arg) {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.splitDynAcceptanceStatuses(arg));
	}
	
	@Test
	void testSplitDynExtensions() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensions("[\n[a0]\n[a1]\n]\n");
		assertEquals(Stream.of("[a0]", "[a1]").collect(Collectors.toList()), extensions);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"[\n", "]\n]\\n", "[\n[\n"})
	void testSplitDynExtensionsErr(final String arg) {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.splitDynExtensions(arg));
	}
	
	@Test
	void testSplitDynExtensionSets() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensionSets("[\n[\n[a0]\n]\n[\n[a1]\n]\n]\n");
		assertEquals(Stream.of("[\n[a0]\n]", "[\n[a1]\n]").collect(Collectors.toList()), extensions);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"[\n", "]\n]\n", "[\n[\n"})
	void testSplitDynExtensionSetsErr(final String arg) {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.splitDynExtensionSets(arg));
	}
	
	@Test
	void testEmptyArgNameErr() throws SyntaxErrorException {
		assertThrows(SyntaxErrorException.class, () -> this.decoder.readExtension("[a0,]"));
	}

}
