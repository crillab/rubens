package fr.cril.rubens.arg.checking.decoders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;

public class SyntaxErrorExceptionTest {
	
	@Test
	public void testMsg() {
		final SyntaxErrorException e = new SyntaxErrorException("foo");
		assertEquals("foo", e.getMessage());
	}

}
