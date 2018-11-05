package fr.cril.rubens.arg.core;

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

}