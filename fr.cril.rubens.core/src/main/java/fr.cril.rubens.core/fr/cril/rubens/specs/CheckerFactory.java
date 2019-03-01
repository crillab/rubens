package fr.cril.rubens.specs;

import java.nio.file.Path;

import fr.cril.rubens.core.CheckResult;
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
	 * Sets the options dedicated to the checker itself.
	 * 
	 * @param options the options as a string
	 */
	void setOptions(String options);
	
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

}
