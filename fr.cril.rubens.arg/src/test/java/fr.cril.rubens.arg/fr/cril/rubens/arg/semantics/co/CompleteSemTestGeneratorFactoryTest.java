package fr.cril.rubens.arg.semantics.co;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;

public class CompleteSemTestGeneratorFactoryTest {
	
	@Test
	public void testInitInstance() {
		assertEquals(new ArgumentationFramework(), new CompleteSemTestGeneratorFactory().initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<ArgumentationFramework>> translators = new CompleteSemTestGeneratorFactory().translators();
		assertEquals(2, translators.size());
		assertTrue(translators.stream().anyMatch(t -> t.getClass().equals(fr.cril.rubens.arg.semantics.co.NewArgTranslator.class)));
		assertTrue(translators.stream().anyMatch(t -> t.getClass().equals(NewAttackTranslator.class)));
	}

}
