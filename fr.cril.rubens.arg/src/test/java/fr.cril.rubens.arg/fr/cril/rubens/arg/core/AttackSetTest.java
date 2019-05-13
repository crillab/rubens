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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class AttackSetTest {
	
	private Attack att1;
	
	private Attack att2;

	private AttackSet set1;

	private AttackSet set2;

	@Before
	public void setUp() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		this.att1 = Attack.getInstance(arg1, arg2);
		this.set1 = AttackSet.getInstance(Collections.singleton(this.att1));
		this.att2 = Attack.getInstance(arg2, arg1);
		this.set2 = AttackSet.getInstance(Collections.singleton(this.att2));
	}
	
	@Test
	public void testGetInstance() {
		assertTrue(AttackSet.getInstance(Collections.singleton(this.att1)) == this.set1);
		assertFalse(AttackSet.getInstance(Collections.singleton(this.att1)) == this.set2);
	}
	
	@Test
	public void testEquals() {
		assertEquals(this.set1, AttackSet.getInstance(Collections.singleton(this.att1)));
		assertNotEquals(this.set2, AttackSet.getInstance(Collections.singleton(this.att1)));
	}
	
	@Test
	public void testHashcode() {
		assertEquals(this.set1.hashCode(), AttackSet.getInstance(Collections.singleton(this.att1)).hashCode());
		assertNotEquals(this.set2.hashCode(), AttackSet.getInstance(Collections.singleton(this.att1)).hashCode());
	}
	
	@Test
	public void testStream() {
		assertEquals(Collections.singleton(this.att1), this.set1.stream().collect(Collectors.toSet()));
	}
	
	@Test
	public void testContains() {
		assertTrue(this.set1.contains(this.att1));
		assertFalse(this.set1.contains(this.att2));
	}
	
	@Test
	public void testSize() {
		assertEquals(0, ArgumentSet.getInstance(Collections.emptySet()).size());
		assertEquals(1, this.set1.size());
	}
	
	@Test
	public void testCollector() {
		assertEquals(this.set1, this.set1.stream().collect(AttackSet.collector()));
		assertEquals(AttackSet.getInstance(Stream.of(this.att1, this.att2).collect(Collectors.toSet())), Stream.of(this.att1, this.att2).parallel().collect(AttackSet.collector()));
	}
	
	@Test
	public void testCollectorCombiner() {
		final BinaryOperator<Set<Attack>> combiner = AttackSet.collector().combiner();
		assertEquals(Stream.of(this.att1, this.att2).collect(Collectors.toSet()), combiner.apply(Stream.of(this.att1).collect(Collectors.toSet()), Stream.of(this.att2).collect(Collectors.toSet())));
	}
	
	@Test
	public void testToString() {
		assertEquals(Collections.singleton(this.att1).toString(), this.set1.toString());
	}
	
	@Test
	public void testRangeOfEmptyAF() {
		final ArgumentSet emptyArgSet = ArgumentSet.getInstance(Collections.emptySet());
		assertEquals(emptyArgSet, AttackSet.getInstance(Collections.emptySet()).rangeOf(emptyArgSet));
	}
	
	@Test
	public void testRange() {
		final ArgumentSet args = Stream.of(Argument.getInstance("a1"), Argument.getInstance("a2")).collect(ArgumentSet.collector());
		assertEquals(args, this.set1.rangeOf(args));
	}

}
