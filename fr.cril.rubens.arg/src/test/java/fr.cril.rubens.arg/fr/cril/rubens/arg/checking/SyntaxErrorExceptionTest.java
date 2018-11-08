package fr.cril.rubens.arg.checking;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SyntaxErrorExceptionTest {
	
	@Test
	public void testMsg() {
		final SyntaxErrorException e = new SyntaxErrorException("foo");
		assertEquals("foo", e.getMessage());
	}

}
