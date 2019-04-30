package fr.cril.rubens.cnf.ddnnf;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.core.ASatCheckerFactory;
import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.SatSolverCheckerFactory;
import fr.cril.rubens.cnf.utils.CommonOptions;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.Option;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(name="DDNNF", family="CNF/KC")
public class DDNNFCheckerFactory extends ASatCheckerFactory<CnfInstance> {
	
	private SatSolverCheckerFactory satFactory = new SatSolverCheckerFactory();
	
	private boolean ignorePreambleErrors = false;
	
	/**
	 * Builds a new factory instance.
	 */
	public DDNNFCheckerFactory() {
		super();
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
		final DDNNFReader reader = new DDNNFReader(this.ignorePreambleErrors);
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
	
	@Override
	public List<Option> getOptions() {
		return Stream.concat(super.getOptions().stream(), Stream.of(
			new Option("ignorePreamble", "ignore wrong values for preamble", v -> this.ignorePreambleErrors = CommonOptions.strToBool(v))
		)).collect(Collectors.toUnmodifiableList());
	}

}
