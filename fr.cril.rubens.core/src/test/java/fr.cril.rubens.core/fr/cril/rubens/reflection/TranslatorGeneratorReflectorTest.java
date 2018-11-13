package fr.cril.rubens.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.testutils.DynamicReflectorParam;
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

	@Test
	public void testNoAnnotation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		final Method method = Class.class.getDeclaredMethod("annotationData", (Class<?>[]) null);
		method.setAccessible(true);
		final Object annotationData = method.invoke(UnbuildableFactory.class);
		final Field annotations = annotationData.getClass().getDeclaredField("annotations");
		annotations.setAccessible(true);
		@SuppressWarnings("unchecked")
		final Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
		map.remove(ReflectorParam.class);
		boolean testFail = true;
		try {
			TranslatorGeneratorReflector.getInstance().resetClasses();
		} catch(IllegalStateException e) {
			testFail = false;
		} finally {
			map.put(ReflectorParam.class, new DynamicReflectorParam(false, ""));
			annotations.setAccessible(false);
			method.setAccessible(false);
		}
		if(testFail) {
			fail();
		}
	}

	@Test
	public void testEnabled() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		final Method method = Class.class.getDeclaredMethod("annotationData", (Class<?>[]) null);
		method.setAccessible(true);
		final Object annotationData = method.invoke(UnbuildableFactory.class);
		final Field annotations = annotationData.getClass().getDeclaredField("annotations");
		annotations.setAccessible(true);
		@SuppressWarnings("unchecked")
		final Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
		map.put(ReflectorParam.class, new DynamicReflectorParam(true, "foo"));
		final TranslatorGeneratorReflector reflector = TranslatorGeneratorReflector.getInstance();
		reflector.resetClasses();
		map.put(ReflectorParam.class, new DynamicReflectorParam(false, ""));
		annotations.setAccessible(false);
		method.setAccessible(false);
		assertEquals(1, reflector.classesNames().size());
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
			return null;
		}

		@Override
		public List<InstanceTranslator<Instance>> translators() {
			return null;
		}

	}

}
