package fr.cril.rubens.testutils;

import java.nio.file.Path;
import java.util.List;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.Option;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(enabled=false)
public class StringConcatCheckerFactory implements CheckerFactory<StringInstance> {

	@Override
	public TestGeneratorFactory<StringInstance> newTestGenerator() {
		return new StringConcatGeneratorFactory();
	}

	@Override
	public CheckResult checkSoftwareOutput(final StringInstance instance, final String result) {
		return CheckResult.SUCCESS;
	}
	
	@Override
	public ASoftwareExecutor<StringInstance> newExecutor(final Path execPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Option> getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ignoreInstance(StringInstance instance) {
		// TODO Auto-generated method stub
		return false;
	}

}
