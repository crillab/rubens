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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.ICCMA19SolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;

public class ICCMA19SolverOutputDecoderTest {
	
	private ICCMA19SolverOutputDecoder decoder;

	@Before
	public void setUp() {
		Forget.all();
		this.decoder = new ICCMA19SolverOutputDecoder();
	}
	
	@Test
	public void testEmptyExtensionSet() throws SyntaxErrorException {
		final ExtensionSet actual = this.decoder.readExtensionSet(""
				+ "[\n"
				+ "  []\n"
				+ "]\n");
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), actual);
	}
	
	@Test
	public void testAcceptanceTrue() {
		assertTrue(this.decoder.isAcceptanceTrue("YES\n"));
	}
	
	@Test
	public void testD3AllEmpty() throws SyntaxErrorException {
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
	public void testD3SingleArg() throws SyntaxErrorException {
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
	
	@Test(expected=SyntaxErrorException.class)
	public void testReadStrExtListOneLine() throws SyntaxErrorException {
		this.decoder.readStrExtensionList("[[a0]]");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testReadStrExtListNoOpeningBracket() throws SyntaxErrorException {
		this.decoder.readStrExtensionList("]\n]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testReadStrExtListNoClosingBracket() throws SyntaxErrorException {
		this.decoder.readStrExtensionList("[\n[\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitExtSetUnexpectedClosingBracket() throws SyntaxErrorException {
		this.decoder.splitExtensionSets("[\n[\n[a0]\n]\n]\n]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitExtSetMissingClosingBracket() throws SyntaxErrorException {
		this.decoder.splitExtensionSets("[\n[\n[a0]\n]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitExtSetUnexpectedArg() throws SyntaxErrorException {
		this.decoder.splitExtensionSets("[\n[\n[a0]\n]\n]\na1\n");
	}
	
	@Test
	public void testNormalizeresult() {
		assertEquals("", this.decoder.normalizeResult(""));
	}
	
	@Test
	public void testDynAcceptanceStatuses() throws SyntaxErrorException {
		final List<String> statuses = this.decoder.splitDynAcceptanceStatuses("[YES, NO]");
		assertEquals(Stream.of("YES", "NO").collect(Collectors.toList()), statuses);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testDynAcceptanceStatusesTooShort() throws SyntaxErrorException {
		this.decoder.splitDynAcceptanceStatuses("[");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testDynAcceptanceStatusesNoOpeningBracket() throws SyntaxErrorException {
		this.decoder.splitDynAcceptanceStatuses("]]");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testDynAcceptanceStatusesNoClosingBracket() throws SyntaxErrorException {
		this.decoder.splitDynAcceptanceStatuses("[[");
	}
	
	@Test
	public void testSplitDynExtensions() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensions("[\n[a0]\n[a1]\n]\n");
		assertEquals(Stream.of("[a0]", "[a1]").collect(Collectors.toList()), extensions);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionsTooShort() throws SyntaxErrorException {
		this.decoder.splitDynExtensions("[\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionsNoOpeningBracket() throws SyntaxErrorException {
		this.decoder.splitDynExtensions("]\n]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionsNoClosingBracket() throws SyntaxErrorException {
		this.decoder.splitDynExtensions("[\n[\n");
	}
	
	@Test
	public void testSplitDynExtensionSets() throws SyntaxErrorException {
		final List<String> extensions = this.decoder.splitDynExtensionSets("[\n[\n[a0]\n]\n[\n[a1]\n]\n]\n");
		assertEquals(Stream.of("[\n[a0]\n]", "[\n[a1]\n]").collect(Collectors.toList()), extensions);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionSetsTooShort() throws SyntaxErrorException {
		this.decoder.splitDynExtensionSets("[\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionSetsNoOpeningBracket() throws SyntaxErrorException {
		this.decoder.splitDynExtensionSets("]\n]\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testSplitDynExtensionSetsNoClosingBracket() throws SyntaxErrorException {
		this.decoder.splitDynExtensionSets("[\n[\n");
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testEmptyArgName() throws SyntaxErrorException {
		this.decoder.readExtension("[a0,]");
	}

}
