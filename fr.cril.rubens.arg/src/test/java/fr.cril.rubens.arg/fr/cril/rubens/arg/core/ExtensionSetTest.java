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

public class ExtensionSetTest {
	
	private ArgumentSet set1;
	
	private ArgumentSet set2;

	private ExtensionSet extset1;

	private ExtensionSet extset2;

	@Before
	public void setUp() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		this.set1 = ArgumentSet.getInstance(Collections.singleton(arg1));
		this.extset1 = ExtensionSet.getInstance(Collections.singleton(this.set1));
		this.set2 = ArgumentSet.getInstance(Collections.singleton(arg2));
		this.extset2 = ExtensionSet.getInstance(Collections.singleton(this.set2));
	}
	
	@Test
	public void testGetInstance() {
		assertTrue(ExtensionSet.getInstance(Collections.singleton(this.set1)) == this.extset1);
		assertFalse(ExtensionSet.getInstance(Collections.singleton(this.set1)) == this.extset2);
	}
	
	@Test
	public void testEquals() {
		assertEquals(this.extset1, ExtensionSet.getInstance(Collections.singleton(this.set1)));
		assertNotEquals(this.extset2, ExtensionSet.getInstance(Collections.singleton(this.set1)));
	}
	
	@Test
	public void testHashcode() {
		assertEquals(this.extset1.hashCode(), ExtensionSet.getInstance(Collections.singleton(this.set1)).hashCode());
		assertNotEquals(this.extset2.hashCode(), ExtensionSet.getInstance(Collections.singleton(this.set1)).hashCode());
	}
	
	@Test
	public void testStream() {
		assertEquals(Collections.singleton(this.set1), this.extset1.stream().collect(Collectors.toSet()));
	}
	
	@Test
	public void testContains() {
		assertTrue(this.extset1.contains(this.set1));
		assertFalse(this.extset1.contains(this.set2));
	}
	
	@Test
	public void testSize() {
		assertEquals(0, ArgumentSet.getInstance(Collections.emptySet()).size());
		assertEquals(1, this.extset1.size());
	}
	
	@Test
	public void testCollector() {
		assertEquals(this.extset1, this.extset1.stream().collect(ExtensionSet.collector()));
		assertEquals(ExtensionSet.getInstance(Stream.of(this.set1, this.set2).collect(Collectors.toSet())), Stream.of(this.set1, this.set2).parallel().collect(ExtensionSet.collector()));
	}
	
	@Test
	public void testCollectorCombiner() {
		final BinaryOperator<Set<ArgumentSet>> combiner = ExtensionSet.collector().combiner();
		assertEquals(Stream.of(this.set1, this.set2).collect(Collectors.toSet()), combiner.apply(Stream.of(this.set1).collect(Collectors.toSet()), Stream.of(this.set2).collect(Collectors.toSet())));
	}
	
	@Test
	public void testToString() {
		assertEquals(Collections.singleton(this.set1).toString(), this.extset1.toString());
	}

}
