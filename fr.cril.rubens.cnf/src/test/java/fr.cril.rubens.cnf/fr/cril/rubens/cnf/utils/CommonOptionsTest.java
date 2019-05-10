package fr.cril.rubens.cnf.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.cnf.core.ASatCheckerFactory;
import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class CommonOptionsTest {
	
	@Test
	public void testSingleton() {
		assertTrue(CommonOptions.getInstance() == CommonOptions.getInstance());
	}
	
	@Test
	public void testGetOptions() {
		assertEquals(1, CommonOptions.getInstance().getOptions(new LocalFactory()).size());
	}
	
	@Test
	public void testIgnUnsatTrue() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("on");
		assertTrue(factory.getIgnUnsat());
	}
	
	@Test
	public void testIgnUnsatFalse() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("off");
		assertFalse(factory.getIgnUnsat());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIgnUnsatWrongVal() {
		final LocalFactory factory = new LocalFactory();
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("ignoreUnsat").apply("foo");
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
