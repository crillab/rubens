package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.specs.InstanceTranslator;

public class GroundedSemTestGeneratorFactoryTest {
	
	@Test
	public void testInitInstance() {
		assertEquals(new ArgumentationFramework(), new GroundedSemTestGeneratorFactory().initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<ArgumentationFramework>> translators = new GroundedSemTestGeneratorFactory().translators();
		assertEquals(2, translators.size());
		assertTrue(translators.stream().anyMatch(t -> t.getClass().equals(NewArgTranslator.class)));
		assertEquals(1, translators.stream().filter(t -> t.getClass().equals(NewAttackTranslator.class)).map(t -> (NewAttackTranslator) t).map(NewAttackTranslator::getExtensionSetComputer)
				.filter(c -> c == EExtensionSetComputer.GROUNDED_SEM).count());
	}

}