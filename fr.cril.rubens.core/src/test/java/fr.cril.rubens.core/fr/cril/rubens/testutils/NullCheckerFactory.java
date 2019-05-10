package fr.cril.rubens.testutils;

import java.nio.file.Path;
import java.util.List;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
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
