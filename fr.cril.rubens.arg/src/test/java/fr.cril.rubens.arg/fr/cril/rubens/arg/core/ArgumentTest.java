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
