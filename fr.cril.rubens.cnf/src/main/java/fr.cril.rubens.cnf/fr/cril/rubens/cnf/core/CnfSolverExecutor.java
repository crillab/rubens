package fr.cril.rubens.cnf.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A software executor dedicated to solvers taking a CNF as input.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the effective type of CNF instance under consideration.
 */
public class CnfSolverExecutor<T extends CnfInstance> extends ASoftwareExecutor<T> {

	/**
	 * Builds an instance of the SAT solver executor given the path to the SAT solver.
	 * 
	 * The SAT solver must take exactly one argument, which is the path to the instance to solve.
	 * 
	 * @param execPath the path to the SAT solver
	 */
	public CnfSolverExecutor(final Path execPath) {
		super(execPath);
	}

	@Override
	protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final T instance) {
		final String execPathStr = execLocation.toAbsolutePath().toString();
		final String cnfPathStr = instanceFiles.get(CnfInstance.CNF_EXT).toAbsolutePath().toString();
		return Stream.of(execPathStr, cnfPathStr).collect(Collectors.toList());
	}

}
