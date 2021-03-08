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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtensionSetTest {
	
	private ArgumentSet set1;
	
	private ArgumentSet set2;

	private ExtensionSet extset1;

	private ExtensionSet extset2;

	@BeforeEach
	public void setUp() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		this.set1 = ArgumentSet.getInstance(Collections.singleton(arg1));
		this.extset1 = ExtensionSet.getInstance(Collections.singleton(this.set1));
		this.set2 = ArgumentSet.getInstance(Collections.singleton(arg2));
		this.extset2 = ExtensionSet.getInstance(Collections.singleton(this.set2));
	}
	
	@Test
	void testGetInstance() {
		assertSame(ExtensionSet.getInstance(Collections.singleton(this.set1)), this.extset1);
		assertNotSame(ExtensionSet.getInstance(Collections.singleton(this.set1)), this.extset2);
	}
	
	@Test
	void testEquals() {
		assertEquals(this.extset1, ExtensionSet.getInstance(Collections.singleton(this.set1)));
		assertNotEquals(this.extset2, ExtensionSet.getInstance(Collections.singleton(this.set1)));
	}
	
	@Test
	void testHashcode() {
		assertEquals(this.extset1.hashCode(), ExtensionSet.getInstance(Collections.singleton(this.set1)).hashCode());
		assertNotEquals(this.extset2.hashCode(), ExtensionSet.getInstance(Collections.singleton(this.set1)).hashCode());
	}
	
	@Test
	void testStream() {
		assertEquals(Collections.singleton(this.set1), this.extset1.stream().collect(Collectors.toSet()));
	}
	
	@Test
	void testContains() {
		assertTrue(this.extset1.contains(this.set1));
		assertFalse(this.extset1.contains(this.set2));
	}
	
	@Test
	void testSize() {
		assertEquals(0, ArgumentSet.getInstance(Collections.emptySet()).size());
		assertEquals(1, this.extset1.size());
	}
	
	@Test
	void testCollector() {
		assertEquals(this.extset1, this.extset1.stream().collect(ExtensionSet.collector()));
		assertEquals(ExtensionSet.getInstance(Stream.of(this.set1, this.set2).collect(Collectors.toSet())), Stream.of(this.set1, this.set2).parallel().collect(ExtensionSet.collector()));
	}
	
	@Test
	void testCollectorCombiner() {
		final BinaryOperator<Set<ArgumentSet>> combiner = ExtensionSet.collector().combiner();
		assertEquals(Stream.of(this.set1, this.set2).collect(Collectors.toSet()), combiner.apply(Stream.of(this.set1).collect(Collectors.toSet()), Stream.of(this.set2).collect(Collectors.toSet())));
	}
	
	@Test
	void testToString() {
		assertEquals(Collections.singleton(this.set1).toString(), this.extset1.toString());
	}

}
