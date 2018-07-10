package fr.cril.rubens.cnf.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class NewVarTranslatorTest {
	
	private NewVarTranslator translator;

	@Before
	public void setUp() {
		this.translator = new NewVarTranslator();
	}
	
	@Test
	public void testCanBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()),
				Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.emptySet());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars()+1, newInstance.nVars());
		assertEquals(instance.clauses(), newInstance.clauses());
		final Set<Set<Integer>> expected = Stream.of(Stream.of(1, -2).collect(Collectors.toSet()), Stream.of(1, 2).collect(Collectors.toSet())) .collect(Collectors.toSet());
		assertEquals(expected, newInstance.models());
	}

}
