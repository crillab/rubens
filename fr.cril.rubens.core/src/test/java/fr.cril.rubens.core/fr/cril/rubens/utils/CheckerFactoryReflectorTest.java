package fr.cril.rubens.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Test;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.testutils.StringConcatCheckerFactory;

public class CheckerFactoryReflectorTest {
	
	@Test
	public void testSingleton() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		assertTrue(instance == CheckerFactoryReflector.getInstance());
	}
	
	@Test
	public void testNoFactory() {
		final CheckerFactoryReflector reflector = CheckerFactoryReflector.getInstance();
		assertTrue(reflector.classesNames().isEmpty());
	}
	
	@Test
	public void testOneFactory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
		final Collection<String> names = instance.classesNames();
		assertEquals(1, names.size());
		assertEquals("testFactory", names.iterator().next());
		assertEquals(StringConcatCheckerFactory.class, instance.getClassInstance("testFactory").getClass());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSameName() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
		instance.addClass("testFactory", StringConcatCheckerFactory.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnexistingFactory( ) {
		CheckerFactoryReflector.getInstance().getClassInstance("toto");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnbuildableFactory() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("testFactory", UnbuildableFactory.class);
		instance.getClassInstance("testFactory");
	}
	
	@After
	public void tearDown() {
		CheckerFactoryReflector.getInstance().resetClasses();
	}
	
	@CheckerFactoryParams(enabled=false)
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
		public String execSoftware(String exec, Instance instance) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(Instance instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}

}
