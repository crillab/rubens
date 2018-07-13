package fr.cril.rubens.cnf.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class NewClauseTranslatorTest {
	
	private NewClauseTranslator translator;

	@Before
	public void setUp() {
		this.translator = new NewClauseTranslator();
	}
	
	@Test
	public void testCanBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList()))
				.collect(Collectors.toList()), Collections.emptySet());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toSet()), new HashSet<>(newInstance.clauses()));
		assertEquals(Collections.emptySet(), newInstance.models());
	}
	
	@Test
	public void testApplyFreeVar() {
		final CnfInstance instance = new CnfInstance(1, new ArrayList<>(),
				Stream.of(Stream.of(1).collect(Collectors.toSet()), Stream.of(-1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		final boolean posLitClause = Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean posLitModels = Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()).equals(newInstance.models());
		final boolean negLitClause = Stream.of(Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean negLitModels = Stream.of(Stream.of(-1).collect(Collectors.toSet())).collect(Collectors.toSet()).equals(newInstance.models());
		assertTrue(posLitClause || negLitClause);
		assertTrue((!posLitClause) || posLitModels);
		assertTrue((!negLitClause) || negLitModels);
	}
	
	@Test
	public void testEmptyInstance() {
		assertFalse(this.translator.canBeAppliedTo(new CnfInstance()));
	}

}
