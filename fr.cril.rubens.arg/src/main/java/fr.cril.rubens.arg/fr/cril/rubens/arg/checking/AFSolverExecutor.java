package fr.cril.rubens.arg.checking;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.AArgumentationFrameworkGraph;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A software executor for argumentation solvers.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of argumentation instances under consideration;
 */
public class AFSolverExecutor<T extends AArgumentationFrameworkGraph> extends ASoftwareExecutor<T> {
	
	private final String problem;

	/**
	 * Builds a new executor given the path to the solver and the kind of problem under consideration.
	 * 
	 * The problem is described by the string associated to it in ICCMA competitions.
	 * 
	 * @param execPath the path to the solver
	 * @param problem the problem
	 */
	public AFSolverExecutor(final Path execPath, final String problem) {
		super(execPath);
		this.problem = problem;
	}

	@Override
	protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final T instance) {
		final String execLoc = execLocation.toAbsolutePath().toString();
		final String apxLoc = instanceFiles.get(ArgumentationFramework.APX_EXT).toAbsolutePath().toString();
		final List<String> cliArgs = Stream.of(execLoc, "-fo", "apx", "-f", apxLoc, "-p", problem).collect(Collectors.toList());
		final Path apxmPath = instanceFiles.get(DynamicArgumentationFramework.APXM_EXT);
		if(apxmPath != null) {
			Stream.of("-m", apxmPath.toAbsolutePath().toString()).forEach(cliArgs::add);
		}
		if(Stream.of("DC-", "DS-").anyMatch(problem::startsWith)) {
			Stream.of("-a", instance.getArgUnderDecision().getName()).forEach(cliArgs::add);
		}
		return cliArgs;
	}

}
