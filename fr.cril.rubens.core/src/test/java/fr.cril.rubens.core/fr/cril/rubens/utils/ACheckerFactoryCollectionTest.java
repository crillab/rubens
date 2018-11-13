package fr.cril.rubens.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.testutils.DynamicReflectorParam;
import fr.cril.rubens.testutils.NullCheckerFactory;

public class ACheckerFactoryCollectionTest {
	
	private FactoryCollection1 f1;
	
	@Before
	public void setUp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		buildClass("c1", NullCheckerFactory.class);
		CheckerFactoryReflector.getInstance().resetClasses();
		f1 = new FactoryCollection1(Stream.of("c1").collect(Collectors.toList()));
		buildClass("f1", FactoryCollection1.class);
	}
	
	@After
	public void tearDown() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
		killClass(FactoryCollection1.class);
		killClass(NullCheckerFactory.class);
		Class.class.getDeclaredMethod("annotationData", (Class<?>[]) null).setAccessible(false);
		CheckerFactoryReflector.getInstance().resetClasses();
	}
	
	private void buildClass(final String name, final Class<?> clazz) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		final Method method = Class.class.getDeclaredMethod("annotationData", (Class<?>[]) null);
		method.setAccessible(true);
		final Object annotationData = method.invoke(clazz);
		final Field annotations = annotationData.getClass().getDeclaredField("annotations");
		annotations.setAccessible(true);
		@SuppressWarnings("unchecked")
		final Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
		map.put(ReflectorParam.class, new DynamicReflectorParam(true, name));
		annotations.setAccessible(false);
		method.setAccessible(false);
	}
	
	private void killClass(final Class<?> clazz) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException {
		final Method method = Class.class.getDeclaredMethod("annotationData", (Class<?>[]) null);
		method.setAccessible(true);
		final Object annotationData = method.invoke(clazz);
		final Field annotations = annotationData.getClass().getDeclaredField("annotations");
		annotations.setAccessible(true);
		@SuppressWarnings("unchecked")
		final Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
		map.put(ReflectorParam.class, new DynamicReflectorParam(false, null));
		annotations.setAccessible(false);
		method.setAccessible(false);
	}
	
	@Test
	public void testOk() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		assertEquals(1, f1.getNames().size());
		assertNotNull(f1.getFactory("c1"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingFactory() {
		new FactoryCollection1(Stream.of("c2").collect(Collectors.toList()));
	}
	
	@ReflectorParam(enabled=false)
	public class FactoryCollection1 extends ACheckerFactoryCollection<Instance> {

		protected FactoryCollection1(final List<String> factoryNames) {
			super(factoryNames);
		}
		
	}
	

}
