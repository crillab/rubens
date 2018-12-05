package fr.cril.rubens.arg.checking.decoders;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

}
