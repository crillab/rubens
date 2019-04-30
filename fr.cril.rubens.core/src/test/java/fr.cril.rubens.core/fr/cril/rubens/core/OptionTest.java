package fr.cril.rubens.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
	
	private Option opt;
	
	private String str;
	
	@Before
	public void setUp() {
		this.str = "";
		this.opt = new Option("n", "d", v -> this.str = v);
	}
	
	@Test
	public void testName() {
		assertEquals("n", this.opt.getName());
	}
	
	@Test
	public void testDescription() {
		assertEquals("d", this.opt.getDescription());
	}
	
	@Test
	public void testApply() {
		this.opt.apply("ok");
		assertEquals("ok", this.str);
	}

}
