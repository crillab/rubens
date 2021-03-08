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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.utils.Forget;

class AttackTest {
	
	private Attack a1;
	
	private Attack a2;
	
	@BeforeEach
	public void setUp() {
		Forget.all();
		this.a1 = Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b"));
		this.a2 = Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("c"));
	}
	
	@Test
	void testGetAttacker() {
		assertEquals(Argument.getInstance("a"), this.a1.getAttacker());
	}
	
	@Test
	void testGetAttacked() {
		assertEquals(Argument.getInstance("b"), this.a1.getAttacked());
	}
	
	@Test
	void testNullAttacker() {
		final Argument arg = Argument.getInstance("a");
		assertThrows(IllegalArgumentException.class, () -> Attack.getInstance(null, arg));
	}
	
	@Test
	void testNullAttacked() {
		final Argument arg = Argument.getInstance("a");
		assertThrows(IllegalArgumentException.class, () -> Attack.getInstance(arg, null));
	}
	
	@Test
	void testSameInstance() {
		assertSame(this.a1, Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
	}
	
	@Test
	void testEquals() {
		assertEquals(this.a1, Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
		assertNotEquals(this.a2, Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
	}
	
	@Test
	void testHashCode() {
		assertEquals(this.a1.hashCode(), Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")).hashCode());
		assertNotEquals(this.a2.hashCode(), Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")).hashCode());
	}
	
}
