package fr.cril.rubens.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
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
		assertTrue(reflector.classesNames().isEmpty());
	}
	
	@Test
	public void testOneFactory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addClass("testFactory", StringConcatGeneratorFactory.class);
		final Collection<String> names = instance.classesNames();
		assertEquals(1, names.size());
		assertEquals("testFactory", names.iterator().next());
		assertEquals(StringConcatGeneratorFactory.class, instance.getClassInstance("testFactory").getClass());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSameName() {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addClass("testFactory", StringConcatGeneratorFactory.class);
		instance.addClass("testFactory", StringConcatGeneratorFactory.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnexistingFactory( ) {
		TranslatorGeneratorReflector.getInstance().getClassInstance("toto");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnbuildableFactory() {
		final TranslatorGeneratorReflector instance = TranslatorGeneratorReflector.getInstance();
		instance.addClass("testFactory", UnbuildableFactory.class);
		instance.getClassInstance("testFactory");
	}
	
	@After
	public void tearDown() {
		TranslatorGeneratorReflector.getInstance().resetClasses();
	}
	
	@ReflectorParam(enabled=false)
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
