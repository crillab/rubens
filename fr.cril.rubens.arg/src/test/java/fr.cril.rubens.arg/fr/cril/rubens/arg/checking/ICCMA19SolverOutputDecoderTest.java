package fr.cril.rubens.arg.checking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

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

}
