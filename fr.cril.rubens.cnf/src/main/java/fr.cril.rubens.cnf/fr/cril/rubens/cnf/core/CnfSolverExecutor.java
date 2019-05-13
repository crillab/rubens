package fr.cril.rubens.cnf.core;

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
