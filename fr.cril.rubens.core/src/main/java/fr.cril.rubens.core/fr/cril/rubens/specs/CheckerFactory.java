package fr.cril.rubens.specs;

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
