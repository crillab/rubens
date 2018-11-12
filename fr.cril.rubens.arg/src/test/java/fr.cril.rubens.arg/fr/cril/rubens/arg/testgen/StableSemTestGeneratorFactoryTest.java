package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.specs.InstanceTranslator;

public class StableSemTestGeneratorFactoryTest {
	
	@Test
	public void testInitInstance() {
		assertEquals(new ArgumentationFramework(), new StableSemTestGeneratorFactory().initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<ArgumentationFramework>> translators = new StableSemTestGeneratorFactory().translators();
		assertEquals(2, translators.size());
		assertTrue(translators.stream().anyMatch(t -> t.getClass().equals(NewArgTranslator.class)));
		assertEquals(1, translators.stream().filter(t -> t.getClass().equals(NewAttackTranslator.class)).map(t -> (NewAttackTranslator) t).map(NewAttackTranslator::getExtensionSetComputer)
				.filter(c -> c == EExtensionSetComputer.STABLE_SEM).count());
	}
	
	@Test
	public void testAddNewArgOnNoExtAF() {
		ArgumentationFramework af = new ArgumentationFramework();
		assertEquals(Stream.of(ArgumentSet.getInstance(Collections.emptySet())).collect(ExtensionSet.collector()), af.getExtensions());
		final NewArgTranslator newArgTranslator = new NewArgTranslator(EExtensionSetComputer.STABLE_SEM);
		af = newArgTranslator.translate(af);
		assertEquals(Stream.of(ArgumentSet.getInstance(Collections.singleton(Argument.getInstance("a0")))).collect(ExtensionSet.collector()), af.getExtensions());
		final NewAttackTranslator newAttackTranslator = new NewAttackTranslator(EExtensionSetComputer.STABLE_SEM);
		af = newAttackTranslator.translate(af);
		assertEquals(ExtensionSet.getInstance(Collections.emptySet()), af.getExtensions());
		af = newArgTranslator.translate(af);
		assertEquals(ExtensionSet.getInstance(Collections.emptySet()), af.getExtensions());
	}

}
