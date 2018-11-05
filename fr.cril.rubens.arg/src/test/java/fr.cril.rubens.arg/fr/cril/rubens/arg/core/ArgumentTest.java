package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.utils.Forget;

public class ArgumentTest {
	
	private Argument a;
	
	private Argument b;
	
	@Before
	public void setUp() {
		Forget.all();
		this.a = Argument.getInstance("a");
		this.b = Argument.getInstance("b");
	}
	
	@Test
	public void testName() {
		assertEquals("a", this.a.getName());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullName() {
		Argument.getInstance(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyName() {
		Argument.getInstance("");
	}
	
	@Test
	public void testSameInstance() {
		assertTrue(this.a == Argument.getInstance("a"));
	}
	
	@Test
	public void testEquals() {
		assertTrue(this.a.equals(Argument.getInstance("a")));
		assertFalse(this.b.equals(Argument.getInstance("a")));
	}
	
	@Test
	public void testHashCode() {
		assertTrue(this.a.hashCode() == Argument.getInstance("a").hashCode());
		assertFalse(this.b.hashCode() == Argument.getInstance("a").hashCode());
	}

}
