package fr.cril.rubens.arg.checking.checkers;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import fr.cril.rubens.arg.checking.AFSolverExecutor;
import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.arg.core.TriFunction;
import fr.cril.rubens.arg.utils.CommonOptions;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * An abstract checker factory used to factor the code of the several {@link CheckerFactory} instances of this module.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(enabled=false)
public abstract class AbstractElementaryCheckerFactory implements ArgumentationFrameworkCheckerFactory<ArgumentationFramework> {
	
	/** the supplier of test generators */
	private Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier;
	
	/** the function used to check results */
	private TriFunction<ArgumentationFramework, String, ISolverOutputDecoder, CheckResult> resultChecker;

	/** the textual representation of the problem (-p parameter of argumentation solvers) */
	private final String problem;
	
	/** the decoder used to read the solver output */ 
	private ISolverOutputDecoder outputFormatDecoder = SolverOutputDecoderFactory.getDefault().getDecoderInstance();

	/**
	 * Builds a new checker factory given the test generator supplier and the checking function.
	 * 
	 * @param generatorSupplier the generator supplier
	 * @param resultChecker the checking function
	 * @param problem the textual representation of the problem (-p parameter of argumentation solvers)
	 */
	protected AbstractElementaryCheckerFactory(final Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier,
			final TriFunction<ArgumentationFramework, String, ISolverOutputDecoder, CheckResult> resultChecker, final String problem) {
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
		return this.resultChecker.apply(instance, result, this.outputFormatDecoder);
	}
	
	@Override
	public ASoftwareExecutor<ArgumentationFramework> newExecutor(final Path execPath) {
		return new AFSolverExecutor<>(execPath, problem);
	}
	
	@Override
	public List<MethodOption> getOptions() {
		return CommonOptions.getInstance().getOptions(this);
	}

	@Override
	public void setOutputFormat(final ISolverOutputDecoder decoder) {
		this.outputFormatDecoder = decoder;
	}
	
}
