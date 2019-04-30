package fr.cril.rubens.cnf.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class ASatCheckerFactoryTest {
	
	@Test
	public void testIgnoreUnsat() {
		final LocalFactory factory = new LocalFactory();
		factory.getOptions().stream().filter(o -> o.getName().equals("ignoreUnsat")).findFirst().orElseThrow().apply("on");
		assertTrue(factory.getIgnUnsat());
	}
	
	@Test
	public void testMultipleIgn() {
		final LocalFactory factory = new LocalFactory();
		assertFalse(factory.getIgnUnsat());
		factory.ignoreUnsat(true);
		assertTrue(factory.getIgnUnsat());
		factory.ignoreUnsat(true);
		assertTrue(factory.getIgnUnsat());
		factory.ignoreUnsat(false);
		assertFalse(factory.getIgnUnsat());
		factory.ignoreUnsat(false);
		assertFalse(factory.getIgnUnsat());
	}
	
	private class LocalFactory extends ASatCheckerFactory<CnfInstance> {

		@Override
		public TestGeneratorFactory<CnfInstance> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<CnfInstance> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(CnfInstance instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}
		
		private boolean getIgnUnsat() {
			return ignoreInstance(new CnfInstance(1, Stream.of(Collections.singletonList(1), Collections.singletonList(-1)).collect(Collectors.toList()), Collections.emptySet()));
		}
		
	}

}
