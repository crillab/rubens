package fr.cril.rubens.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.specs.TestGeneratorFactoryParams;
import fr.cril.rubens.testutils.StringConcatGeneratorFactory;


public class TranslatorGeneratorReflectorTest {
	
	@Test
	public void testSingleton() {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		assertTrue(instance == TranslatorGeneratorReflector.getInstance());
	}
	
	@Test
	public void testNoFactory() {
		final TranslatorGeneratorReflector reflector = TranslatorGeneratorReflector.getInstance();
		assertTrue(reflector.factoryNames().isEmpty());
	}
	
	@Test
	public void testOneFactory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addFactory("testFactory", StringConcatGeneratorFactory.class);
		final Collection<String> names = instance.factoryNames();
		assertEquals(1, names.size());
		assertEquals("testFactory", names.iterator().next());
		assertEquals(StringConcatGeneratorFactory.class, instance.getFactory("testFactory").getClass());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSameName() {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addFactory("testFactory", StringConcatGeneratorFactory.class);
		instance.addFactory("testFactory", StringConcatGeneratorFactory.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnexistingFactory( ) {
		TranslatorGeneratorReflector.getInstance().getFactory("toto");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnbuildableFactory() {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addFactory("testFactory", UnbuildableFactory.class);
		instance.getFactory("testFactory");
	}
	
	@After
	public void tearDown() {
		TranslatorGeneratorReflector.getInstance().resetFactories();
	}
	
	@TestGeneratorFactoryParams(enabled=false)
	private class UnbuildableFactory implements TestGeneratorFactory<Instance> {
		
		private UnbuildableFactory(final Object nevermind) {
			// nothing to do here
		}

		@Override
		public Instance initInstance() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<InstanceTranslator<Instance>> translators() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
