package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.Option;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class ArgumentationFrameworkCheckerFactoryTest {
	
	@Test
	public void testIgnFilters() {
		assertFalse(new LocalFactory().ignoreInstance(new ArgumentationFramework()));
	}
	
	@ReflectorParam(enabled=false)
	private class LocalFactory implements ArgumentationFrameworkCheckerFactory<AArgumentationFrameworkGraph> {

		@Override
		public TestGeneratorFactory<AArgumentationFrameworkGraph> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<AArgumentationFrameworkGraph> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(AArgumentationFrameworkGraph instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Option> getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setOutputFormat(ISolverOutputDecoder decoder) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
