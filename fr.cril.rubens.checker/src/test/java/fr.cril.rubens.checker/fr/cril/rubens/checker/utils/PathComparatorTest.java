package fr.cril.rubens.checker.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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

public class PathComparatorTest {
	
	private PathComparator comp;

	@Before
	public void setUp() {
		this.comp = new PathComparator();
	}
	
	@Test
	public void testSame() {
		assertEquals(0, this.comp.compare("/foo/bar", "/foo/bar"));
	}
	
	@Test
	public void testDifferent1() {
		assertTrue(this.comp.compare("/foo/1/bar", "/foo/2/bar") < 0);
	}
	
	@Test
	public void testDifferent2() {
		assertTrue(this.comp.compare("/foo/2/bar", "/foo/1/bar") > 0);
	}
	
	@Test
	public void testLonger1() {
		assertTrue(this.comp.compare("/foo/bar", "/foo/bar/foobar") < 0);
	}
	
	@Test
	public void testLonger2() {
		assertTrue(this.comp.compare("/foo/bar/foobar", "/foo/bar") > 0);
	}

}
