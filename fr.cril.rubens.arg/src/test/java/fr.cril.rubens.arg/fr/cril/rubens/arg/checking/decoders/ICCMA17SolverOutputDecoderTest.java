package fr.cril.rubens.arg.checking.decoders;

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
