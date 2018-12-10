package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;

public class D3TestGeneratorFactoryTest {
	
	@Test
	public void testInit() {
		final D3TestGeneratorFactory factory = new D3TestGeneratorFactory();
		assertEquals(ArgumentSet.getInstance(Collections.emptySet()), factory.initInstance().getArguments());
		assertEquals(2, factory.translators().size());
	}
	
	@Test
	public void testTranslateSome() {
		final D3TestGeneratorFactory factory = new D3TestGeneratorFactory();
		final Set<D3ArgumentationFramework> generated = new HashSet<>();
		generated.add(factory.initInstance());
		for(final InstanceTranslator<D3ArgumentationFramework> translator: factory.translators()) {
			final List<D3ArgumentationFramework> newAFs = generated.stream().filter(i -> translator.canBeAppliedTo(i)).map(translator::translate).collect(Collectors.toList());
			generated.addAll(newAFs);
		}
		for(final InstanceTranslator<D3ArgumentationFramework> translator: factory.translators()) {
			final List<D3ArgumentationFramework> newAFs = generated.stream().filter(i -> translator.canBeAppliedTo(i)).map(translator::translate).collect(Collectors.toList());
			generated.addAll(newAFs);
		}
	}

}
