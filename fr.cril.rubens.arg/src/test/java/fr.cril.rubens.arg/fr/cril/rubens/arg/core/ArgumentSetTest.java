package fr.cril.rubens.arg.core;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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

public class ArgumentSetTest {
	
	private Argument a1;
	
	private Argument a2;

	private ArgumentSet set1;

	private ArgumentSet set2;

	@Before
	public void setUp() {
		this.a1 = Argument.getInstance("a1");
		this.set1 = ArgumentSet.getInstance(Collections.singleton(this.a1));
		this.a2 = Argument.getInstance("a2");
		this.set2 = ArgumentSet.getInstance(Collections.singleton(this.a2));
	}
	
	@Test
	public void testGetInstance() {
		assertTrue(ArgumentSet.getInstance(Collections.singleton(this.a1)) == this.set1);
		assertFalse(ArgumentSet.getInstance(Collections.singleton(this.a1)) == this.set2);
	}
	
	@Test
	public void testEquals() {
		assertEquals(this.set1, ArgumentSet.getInstance(Collections.singleton(this.a1)));
		assertNotEquals(this.set2, ArgumentSet.getInstance(Collections.singleton(this.a1)));
	}
	
	@Test
	public void testHashcode() {
		assertEquals(this.set1.hashCode(), ArgumentSet.getInstance(Collections.singleton(this.a1)).hashCode());
		assertNotEquals(this.set2.hashCode(), ArgumentSet.getInstance(Collections.singleton(this.a1)).hashCode());
	}
	
	@Test
	public void testStream() {
		assertEquals(Collections.singleton(this.a1), this.set1.stream().collect(Collectors.toSet()));
	}
	
	@Test
	public void testContains() {
		assertTrue(this.set1.contains(this.a1));
		assertFalse(this.set1.contains(this.a2));
	}
	
	@Test
	public void testSize() {
		assertEquals(0, ArgumentSet.getInstance(Collections.emptySet()).size());
		assertEquals(1, this.set1.size());
	}
	
	@Test
	public void testCollector() {
		assertEquals(this.set1, this.set1.stream().collect(ArgumentSet.collector()));
		assertEquals(ArgumentSet.getInstance(Stream.of(this.a1, this.a2).collect(Collectors.toSet())), Stream.of(this.a1, this.a2).parallel().collect(ArgumentSet.collector()));
	}
	
	@Test
	public void testCollectorCombiner() {
		final BinaryOperator<Set<Argument>> combiner = ArgumentSet.collector().combiner();
		assertEquals(Stream.of(this.a1, this.a2).collect(Collectors.toSet()), combiner.apply(Stream.of(this.a1).collect(Collectors.toSet()), Stream.of(this.a2).collect(Collectors.toSet())));
	}
	
	@Test
	public void testToString() {
		assertEquals(Collections.singleton(this.a1).toString(), this.set1.toString());
	}
	
	@Test
	public void testSupersetOfEmpty() {
		assertTrue(this.set1.isSupersetOf(ArgumentSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	public void testSupersetOfNonEmpty() {
		assertTrue(Stream.of(this.a1, this.a2).collect(ArgumentSet.collector()).isSupersetOf(ArgumentSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	public void testNotSupersetOfSubset() {
		assertFalse(this.set1.isSupersetOf(Stream.of(this.a1, this.a2).collect(ArgumentSet.collector())));
	}
	
	@Test
	public void testEmptyIsNotSuperset() {
		assertFalse(ArgumentSet.getInstance(Collections.emptySet()).isSupersetOf(this.set1));
	}
	
	@Test
	public void testIsNotSupersetOfIdentity() {
		assertFalse(this.set1.isSupersetOf(this.set1));
	}
	
	@Test
	public void testIsNotSupersetSameArity() {
		assertFalse(this.set1.isSupersetOf(this.set2));
	}
	
	@Test
	public void testIsNotSupersetAtAll() {
		final Argument a3 = Argument.getInstance("a3");
		final ArgumentSet set13 = Stream.of(this.a1, a3).collect(ArgumentSet.collector());
		assertFalse(this.set2.isSupersetOf(set13));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSupersetOfNull() {
		this.set1.isSupersetOf(null);
	}

}
