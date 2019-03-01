package fr.cril.rubens.testutils;

import java.nio.file.Path;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(enabled=false)
public class NullCheckerFactory implements CheckerFactory<Instance> {

	@Override
	public TestGeneratorFactory<Instance> newTestGenerator() {
		return null;
	}

	@Override
	public CheckResult checkSoftwareOutput(Instance instance, String result) {
		return null;
	}
	
	@Override
	public void setOptions(final String options) {
		// nothing to do here
	}

	@Override
	public ASoftwareExecutor<Instance> newExecutor(Path execPath) {
		// TODO Auto-generated method stub
		return null;
	}

}
