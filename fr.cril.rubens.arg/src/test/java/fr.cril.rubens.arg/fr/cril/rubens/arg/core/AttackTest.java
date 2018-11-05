package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.utils.Forget;

public class AttackTest {
	
	private Attack a1;
	
	private Attack a2;
	
	@Before
	public void setUp() {
		Forget.all();
		this.a1 = Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b"));
		this.a2 = Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("c"));
	}
	
	@Test
	public void testGetAttacker() {
		assertEquals(Argument.getInstance("a"), this.a1.getAttacker());
	}
	
	@Test
	public void testGetAttacked() {
		assertEquals(Argument.getInstance("b"), this.a1.getAttacked());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullAttacker() {
		Attack.getInstance(null, Argument.getInstance("a"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullAttacked() {
		Attack.getInstance(Argument.getInstance("a"), null);
	}
	
	@Test
	public void testSameInstance() {
		assertTrue(this.a1 == Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
	}
	
	@Test
	public void testEquals() {
		assertTrue(this.a1.equals(Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b"))));
		assertFalse(this.a2.equals(Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b"))));
	}
	
	@Test
	public void testHashCode() {
		assertTrue(this.a1.hashCode() == Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")).hashCode());
		assertFalse(this.a2.hashCode() == Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")).hashCode());
	}
	
}
