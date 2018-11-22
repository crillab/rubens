package fr.cril.rubens.specs;

import fr.cril.rubens.core.CheckResult;

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
	 * Executes the software on an instance and returned its output.
	 * 
	 * @param exec the software location
	 * @param instance the instance
	 * @return the software output
	 */
	String execSoftware(String exec, T instance);
	
	/**
	 * Checks the output of a solver for an instance.
	 * 
	 * @param instance the instances
	 * @param result the software output
	 * @return <code>true</code> iff the solver result matches the expected results
	 */
	CheckResult checkSoftwareOutput(T instance, String result);

}
