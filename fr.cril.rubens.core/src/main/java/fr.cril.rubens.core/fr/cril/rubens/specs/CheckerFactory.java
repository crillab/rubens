package fr.cril.rubens.specs;

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

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * An interface to be implemented by factories building software checkers.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the instance type involved in the checking process
 */
public interface CheckerFactory<T extends Instance> {
	
	/**
	 * Returns a new test generator used to generate the instances used for the checking process.
	 * 
	 * @return a new test generator used to generate the instances used for the checking process
	 */
	TestGeneratorFactory<T> newTestGenerator();
	
	/**
	 * Returns a new object dedicated to instantiate software under checking.
	 * @see ASoftwareExecutor
	 * 
	 * @param execPath the path to he software under checking
	 * @return the software executor
	 */
	ASoftwareExecutor<T> newExecutor(final Path execPath);
	
	/**
	 * Checks the output of a solver for an instance.
	 * 
	 * @param instance the instances
	 * @param result the software output
	 * @return <code>true</code> iff the solver result matches the expected results
	 */
	CheckResult checkSoftwareOutput(T instance, String result);
	
	/**
	 * Returns the checker specific options.
	 * 
	 * @return the checker specific options
	 */
	List<MethodOption> getOptions();
	
	/**
	 * Indicates if an instance should be ignored in the checking process.
	 * 
	 * This does not prevent the instance to derive new instances.
	 * If this behavior should be changed, the test generator must be adapted.
	 * 
	 * @param instance an instance
	 * @return <code>true</code> iff the instance should not be used by the checker
	 */
	boolean ignoreInstance(T instance);

}
