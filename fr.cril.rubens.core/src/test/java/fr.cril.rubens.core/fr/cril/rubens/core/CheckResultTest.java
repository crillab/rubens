package fr.cril.rubens.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckResultTest {
	
	@Test
	public void testSuccessfulInstance() {
		assertTrue(CheckResult.SUCCESS.isSuccessful());
		assertEquals("SUCCESS", CheckResult.SUCCESS.getExplanation());
	}
	
	@Test
	public void testFailedInstance() {
		final CheckResult result = CheckResult.newError("foobar");
		assertFalse(result.isSuccessful());
		assertEquals("foobar", result.getExplanation());
	}
	
	@Test
	public void testAsException() {
		final CheckResult result = CheckResult.newError("foobar");
		assertEquals("foobar", result.asException().getErrorResult().getExplanation());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testSuccessAsException() {
		CheckResult.SUCCESS.asException();
	}

}
