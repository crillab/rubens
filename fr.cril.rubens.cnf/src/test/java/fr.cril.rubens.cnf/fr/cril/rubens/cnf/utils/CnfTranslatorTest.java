package fr.cril.rubens.cnf.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.NewVarTranslator;

public class CnfTranslatorTest {
	
	private NewVarTranslator adapted;
	
	private CnfTranslatorAdapter<CnfInstance, CnfInstance> adapter;

	@Before
	public void setUp() {
		this.adapted = new NewVarTranslator();
		this.adapter = new CnfTranslatorAdapter<>(this.adapted);
	}
	
	@Test
	public void testCanBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		assertTrue(this.adapted.canBeAppliedTo(instance));
		assertTrue(this.adapter.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()),
				Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.emptySet());
		assertFalse(this.adapted.canBeAppliedTo(instance));
		assertFalse(this.adapter.canBeAppliedTo(instance));
	}
	
	@Test
	public void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstanceOfAdapted = this.adapted.translate(instance);
		final CnfInstance newInstanceOfAdapter = this.adapter.translate(instance);
		assertEquals(newInstanceOfAdapted, newInstanceOfAdapter);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalAdapted() {
		final CnfTranslatorAdapter<CnfInstance, DummyCnfInstance> dummyAdapter = new CnfTranslatorAdapter<CnfInstance, DummyCnfInstance>(this.adapted);
		dummyAdapter.translate(new DummyCnfInstance());
	}
	
	private class DummyCnfInstance extends CnfInstance {
		
		private DummyCnfInstance() {
			super();
		}
		
	}

}
