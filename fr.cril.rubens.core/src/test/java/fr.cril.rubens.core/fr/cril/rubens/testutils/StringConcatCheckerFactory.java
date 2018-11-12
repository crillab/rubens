package fr.cril.rubens.testutils;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;

@ReflectorParam(enabled=false)
public class StringConcatCheckerFactory implements CheckerFactory<StringInstance> {

	@Override
	public TestGeneratorFactory<StringInstance> newTestGenerator() {
		return new StringConcatGeneratorFactory();
	}

	@Override
	public String execSoftware(final String exec, final StringInstance instance) {
		return instance.str();
	}

	@Override
	public CheckResult checkSoftwareOutput(final StringInstance instance, final String result) {
		return CheckResult.SUCCESS;
	}

}
