package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;


public class DynamicSemTestGeneratorFactoryDecoratorTest {
	
	private DynamicSemTestGeneratorFactoryDecorator decorator;
	
	private CompleteSemTestGeneratorFactory decorated;
	
	@Before
	public void setUp() {
		this.decorated = new CompleteSemTestGeneratorFactory();
		this.decorator = new DynamicSemTestGeneratorFactoryDecorator(this.decorated, EExtensionSetComputer.COMPLETE_SEM);
	}
	
	@Test
	public void testInitInstance() {
		assertEquals(this.decorated.initInstance(), this.decorator.initInstance().getInitInstance());
	}
	
	@Test
	public void testTranslators() {
		assertEquals(this.decorated.translators().size(), this.decorator.translators().size());
	}
	
	@Test
	public void testNoDyn() {
		final CompleteSemTestGeneratorFactory decorated = new CompleteSemTestGeneratorFactory();
		int index = -1;
		final List<InstanceTranslator<ArgumentationFramework>> decoratedTranslators = decorated.translators();
		for(int i=0; i<decoratedTranslators.size(); ++i) {
			if(decoratedTranslators.get(i).getClass().equals(NewArgTranslator.class)) {
				index = i;
				break;
			}
		}
		assertNotEquals(-1, index);
		final DynamicSemTestGeneratorFactoryDecorator decorator = new DynamicSemTestGeneratorFactoryDecorator(decorated, EExtensionSetComputer.COMPLETE_SEM);
		final InstanceTranslator<DynamicArgumentationFramework> argTranslator = decorator.translators().get(index);
		final InstanceTranslator<DynamicArgumentationFramework> attTranslator = decorator.translators().get(1-index);
		for(int i=0; i<(1<<8); ++i) {
			assertTrue(argTranslator.canBeAppliedTo(decorator.initInstance()));
			final DynamicArgumentationFramework generated = argTranslator.translate(decorator.initInstance());
			assertEquals(1, generated.getTranslations().size());
			assertTrue(attTranslator.canBeAppliedTo(generated));
			final DynamicArgumentationFramework generated2 = attTranslator.translate(generated);
			assertEquals(1, generated2.getTranslations().size());
		}
	}

}
