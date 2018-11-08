package fr.cril.rubens.arg.checking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * An abstract checker factory used to factor the code of the several {@link CheckerFactory} instances of this module.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(enabled=false)
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
		Path apxPath = null;
		try {
			apxPath = Files.createTempFile("rubens-arg-", ".tmp");
			instance.write(".apx", Files.newOutputStream(apxPath));
			final List<String> cliArgs = Stream.of(exec, "-fo", "apx", "-f", apxPath.toAbsolutePath().toString(), "-p", this.problem).collect(Collectors.toList());
			if(Stream.of("DC-", "DS-").anyMatch(this.problem::startsWith)) {
				Stream.of("-a", instance.getArgUnderDecision().getName()).forEach(cliArgs::add);
			}
			final ProcessBuilder pBuilder = new ProcessBuilder(cliArgs);
			pBuilder.directory(Paths.get(exec).getParent().toFile());
			final Process p = pBuilder.start();
			final StringBuilder builder = new StringBuilder();
			final InputStream is = p.getInputStream();
			new Thread(() -> {
				try(final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
					String line;
					while((line = reader.readLine()) != null) {
						builder.append(line);
						builder.append('\n');
					}
				} catch (IOException e) {
					return;
				}
			}).start();
			p.waitFor();
			Files.delete(apxPath);
			return builder.toString();
		} catch(final IOException e) {
			throw new IllegalStateException(e.getMessage());
		} catch(final InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException();
		} finally {
			try {
				if(apxPath != null) {
					Files.delete(apxPath);
				}
			} catch(IOException e) {
				// nothing to do here
			}
		}
	}

}
