package fr.cril.rubens.arg.checking;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

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
