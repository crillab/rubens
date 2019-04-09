package fr.cril.rubens.cnf.mc;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A checker factory dedicated to model counters.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="sharpSAT")
public class ModelCounterCheckerFactory implements CheckerFactory<ModelCountingCnfInstance> {

	@Override
	public void setOptions(String options) {
		// no available options
	}

	@Override
	public TestGeneratorFactory<ModelCountingCnfInstance> newTestGenerator() {
		return new ModelCountingCnfTestGeneratorFactory();
	}

	@Override
	public ASoftwareExecutor<ModelCountingCnfInstance> newExecutor(final Path execPath) {
		return new CnfSolverExecutor<>(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final ModelCountingCnfInstance instance, final String result) {
		String status = null;
		final List<String> lines = Arrays.stream(result.split("\n")).collect(Collectors.toList());
		for(final String line : lines) {
			if(line.startsWith("c")) {
				continue;
			}
			if(line.startsWith("s")) {
				if(status != null) {
					return CheckResult.newError("multiple status line");
				}
				status = line;
			} else {
				return CheckResult.newError("unexpected line: \""+line+"\"");
			}
		}
		if(status == null) {
			return CheckResult.newError("no status line");
		}
		if(status.length() < 2 || status.charAt(1) != ' ') {
			return CheckResult.newError("wrong status line: \""+status+"\"");
		}
		final String strCount = status.substring(2);
		try {
			int count = Integer.parseInt(strCount);
			return count == instance.models().size() ? CheckResult.SUCCESS : CheckResult.newError("wrong model count (got "+count+", expected "+instance.models().size()+")");
		} catch(NumberFormatException e) {
			return CheckResult.newError("wrong number of models provided: \""+strCount+"\"");
		}
	}

}
