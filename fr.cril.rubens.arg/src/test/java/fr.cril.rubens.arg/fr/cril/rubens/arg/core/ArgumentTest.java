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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.utils.Forget;

class ArgumentTest {
	
	private Argument a;
	
	private Argument b;
	
	@BeforeEach
	public void setUp() {
		Forget.all();
		this.a = Argument.getInstance("a");
		this.b = Argument.getInstance("b");
	}
	
	@Test
	void testName() {
		assertEquals("a", this.a.getName());
	}
	
	@Test
	void testNullName() {
		assertThrows(IllegalArgumentException.class, () -> Argument.getInstance(null));
	}
	
	@Test
	void testEmptyName() {
		assertThrows(IllegalArgumentException.class, () -> Argument.getInstance(""));
	}
	
	@Test
	void testSameInstance() {
		assertSame(this.a, Argument.getInstance("a"));
	}
	
	@Test
	void testEquals() {
		assertEquals(this.a, Argument.getInstance("a"));
		assertNotEquals(this.b, Argument.getInstance("a"));
	}
	
	@Test
	void testHashCode() {
		assertSame(this.a.hashCode(), Argument.getInstance("a").hashCode());
		assertNotSame(this.b.hashCode(), Argument.getInstance("a").hashCode());
	}

}
