package fr.cril.rubens.arg.checking;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * An abstract checker factory used to factor the code of the several {@link CheckerFactory} instances of this module.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(enabled=false)
public abstract class AbstractCheckerFactory implements CheckerFactory<ArgumentationFramework> {
	
	/** the supplier of test generators */
	private Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier;
	
	/** the function used to check results */
	private BiFunction<ArgumentationFramework, String, CheckResult> resultChecker;

	/** the textual representation of the problem (-p parameter of argumentation solvers) */
	private final String problem;

	/**
	 * Builds a new checker factory given the test generator supplier and the checking function.
	 * 
	 * @param generatorSupplier the generator supplier
	 * @param resultChecker the checking function
	 * @param the textual representation of the problem (-p parameter of argumentation solvers)
	 */
	protected AbstractCheckerFactory(final Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier, final BiFunction<ArgumentationFramework, String, CheckResult> resultChecker,
			final String problem) {
		this.generatorSupplier = generatorSupplier;
		this.resultChecker = resultChecker;
		this.problem = problem;
	}
	
	@Override
	public TestGeneratorFactory<ArgumentationFramework> newTestGenerator() {
		return this.generatorSupplier.get();
	}

	@Override
	public CheckResult checkSoftwareOutput(final ArgumentationFramework instance, final String result) {
		return this.resultChecker.apply(instance, result);
	}
	
	@Override
	public String execSoftware(final String exec, final ArgumentationFramework instance) {
		return SoftwareExecutor.execSoftware(exec, this.problem, instance);
	}

}
