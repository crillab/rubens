package fr.cril.rubens.reflection;

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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.testutils.StringConcatCheckerFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

class CheckerFactoryReflectorTest {
	
	@Test
	void testSingleton() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		assertSame(instance, CheckerFactoryReflector.getInstance());
	}
	
	@Test
	void testNoFactory() {
		final CheckerFactoryReflector reflector = CheckerFactoryReflector.getInstance();
		assertTrue(reflector.classesNames().isEmpty());
	}
	
	@Test
	void testOneFactory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
		final Collection<String> names = instance.classesNames();
		assertEquals(1, names.size());
		assertEquals("testFactory", names.iterator().next());
		assertEquals(StringConcatCheckerFactory.class, instance.getClassInstance("testFactory").getClass());
	}
	
	@Test
	void testSameName() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
		assertThrows(IllegalStateException.class, () -> instance.addClass("testFactory", StringConcatCheckerFactory.class));
	}
	
	@Test
	void testUnexistingFactory() {
		final CheckerFactoryReflector refl = CheckerFactoryReflector.getInstance();	
		assertThrows(IllegalArgumentException.class, () -> refl.getClassInstance("toto"));
	}
	
	@Test
	void testUnbuildableFactory() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", UnbuildableFactory.class);
		assertThrows(IllegalArgumentException.class, () -> instance.getClassInstance("testFactory"));
	}
	
	@Test
	void testInFamily() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", "foo", StringConcatCheckerFactory.class);
		final Collection<String> families = instance.familiesNames();
		assertEquals(1, families.size());
		assertEquals("foo", families.iterator().next());
		final Collection<String> family = instance.family("foo");
		assertEquals(1, family.size());
		assertEquals("testFactory", family.iterator().next());
		assertEquals(0, instance.withoutFamily().size());
		assertEquals(StringConcatCheckerFactory.class, instance.getClassInstance("testFactory").getClass());
	}
	
	@Test
	void testNotInFamily() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
		assertEquals(0, instance.familiesNames().size());
		final Collection<String> withoutFamily = instance.withoutFamily();
		assertEquals(1, withoutFamily.size());
		assertEquals("testFactory", withoutFamily.iterator().next());
		assertEquals(StringConcatCheckerFactory.class, instance.getClassInstance("testFactory").getClass());
	}
	
	@Test
	void testUnknownFamily() {
		CheckerFactoryReflector refl = CheckerFactoryReflector.getInstance();
		assertThrows(IllegalArgumentException.class, () -> refl.family("foo"));
	}
	
	@AfterEach
	public void tearDown() {
		CheckerFactoryReflector.getInstance().resetClasses();
	}
	
	@ReflectorParam(enabled=false)
	private class UnbuildableFactory implements CheckerFactory<Instance> {
		
		private UnbuildableFactory(final Object nevermind) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public TestGeneratorFactory<Instance> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(Instance instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ASoftwareExecutor<Instance> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<MethodOption> getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean ignoreInstance(Instance instance) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
