package fr.cril.rubens.cnf.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class NewLitInClauseTranslatorTest {
	
	private NewLitInClauseTranslator translator;

	@Before
	public void setUp() {
		this.translator = new NewLitInClauseTranslator();
	}
	
	@Test
	public void testCanBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeAppliedNoClauses() {
		final CnfInstance instance = new CnfInstance(1, new ArrayList<>(), new HashSet<>());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeAppliedFullClauses() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1, -1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptySet());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(Stream.of(Stream.of(1, -1).collect(Collectors.toList())).collect(Collectors.toList()), newInstance.clauses());
		assertEquals(Stream.of(Stream.of(1).collect(Collectors.toSet()), Stream.of(-1).collect(Collectors.toSet())).collect(Collectors.toSet()), newInstance.models());
	}
	
	@Test
	public void testApplyFreeVars() {
		final CnfInstance instance = new CnfInstance(2, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{1, -2}, new int[]{1, 2})
				.map(m -> Arrays.stream(m).boxed().collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(1, newInstance.clauses().size());
		assertEquals(2, newInstance.clauses().iterator().next().size());
		assertTrue(newInstance.models().size() >= 3);
	}
	
	@Test
	public void testApplyFullClause() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(-1, 1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{-1}).map(m -> Arrays.stream(m).boxed().collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(2, newInstance.clauses().size());
		assertEquals(2, newInstance.clauses().iterator().next().size());
		assertEquals(2, newInstance.models().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void applyFullClauses() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(-1, 1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{-1}, new int[]{1}).map(m -> Arrays.stream(m).boxed().collect(Collectors.toSet())).collect(Collectors.toSet()));
		this.translator.translate(instance);
	}

}
