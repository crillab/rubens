package fr.cril.rubens.cnf.ddnnf;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.SatSolverCheckerFactory;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(name="DDNNF")
public class DDNNFCheckerFactory implements CheckerFactory<CnfInstance> {
	
	private SatSolverCheckerFactory satFactory = new SatSolverCheckerFactory();
	
	@Override
	public void setOptions(final String options) {
		this.satFactory.setOptions(options);
	}

	@Override
	public TestGeneratorFactory<CnfInstance> newTestGenerator() {
		return this.satFactory.newTestGenerator();
	}

	@Override
	public ASoftwareExecutor<CnfInstance> newExecutor(final Path execPath) {
		return this.satFactory.newExecutor(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final CnfInstance instance, final String result) {
		final DDNNFReader reader = new DDNNFReader();
		DDNNF formula = null;
		try {
			formula = reader.read(result);
		} catch (final DDNNFException e) {
			return CheckResult.newError(e.getMessage());
		}
		final Set<Set<Integer>> models = new HashSet<>(instance.models());
		try {
			formula.iterateModels(m -> {
				if(!models.remove(m)) {
					throw new CheckErrorException(CheckResult.newError(m.toString()+" is model of the dDNNF but not model of the CNF"));
				}
			});
		} catch(CheckErrorException e) {
			return e.getResult();
		}
		if(!models.isEmpty()) {
			return CheckResult.newError(models.iterator().next().toString()+" is model of the CNF but not model of the dDNNF");
		}
		return CheckResult.SUCCESS;
	}
	
	private class CheckErrorException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		private final CheckResult result;
		
		private CheckErrorException(final CheckResult result) {
			this.result = result;
		}
		
		private CheckResult getResult() {
			return this.result;
		}
	}

}
