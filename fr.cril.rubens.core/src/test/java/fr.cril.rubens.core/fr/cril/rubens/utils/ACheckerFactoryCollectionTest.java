package fr.cril.rubens.utils;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.testutils.DynamicReflectorParam;
import fr.cril.rubens.testutils.NullCheckerFactory;

class ACheckerFactoryCollectionTest {
	
	private FactoryCollection1 f1;
	
	@BeforeEach
	public void setUp() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		buildClass("c1", NullCheckerFactory.class);
		CheckerFactoryReflector.getInstance().resetClasses();
		f1 = new FactoryCollection1(Stream.of("c1").collect(Collectors.toList()));
		buildClass("f1", FactoryCollection1.class);
	}
	
	@AfterEach
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
	void testOk() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		assertEquals(1, f1.getNames().size());
		assertNotNull(f1.getFactory("c1"));
	}
	
	@Test
	void testMissingFactory() {
		List<String> c2 = Stream.of("c2").collect(Collectors.toList());
		assertThrows(IllegalArgumentException.class, () -> new FactoryCollection1(c2));
	}
	
	@ReflectorParam(enabled=false)
	public class FactoryCollection1 extends ACheckerFactoryCollection<Instance> {

		protected FactoryCollection1(final List<String> factoryNames) {
			super(factoryNames);
		}
		
	}
	

}
