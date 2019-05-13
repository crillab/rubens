package fr.cril.rubens.arg.core;

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
