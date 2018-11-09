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
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	/** the logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCheckerFactory.class);
	
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
		final String result = doExecSoftware(exec, instance, false);
		if(result.equals("")) {
			return doExecSoftware(exec, instance, true);
		}
		return result;
	}
	
	/**
	 * Executes the software on an instance and returned its output.
	 * 
	 * It sometimes happen the standard output stream contains no data although it should.
	 * This method provides a flag allowing to wait one second before waiting for the subprocess end;
	 * this sleeping time seems to fix the problem.
	 * 
	 * @param exec the software location
	 * @param instance the instance
	 * @param wait the waiting flag
	 * @return the solver output
	 */
	private String doExecSoftware(final String exec, final ArgumentationFramework instance, final boolean wait) {
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
			launchStreamThread(p.getInputStream(), l -> builder.append(l).append('\n'));
			launchStreamThread(p.getErrorStream(), l -> LOGGER.warn("software wrote to stderr: {}", l));
			p.getOutputStream().close();
			if(wait) {
				Thread.sleep(1000);
			}
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
	
	/**
	 * Launches a stream used to handle {@link ProcessBuilder} input streams.
	 * 
	 * The stream is read line per line; each line is then process by the provided {@link Consumer}.
	 * 
	 * @param is the input stream
	 * @param lineHandler the line consumer
	 */
	private void launchStreamThread(final InputStream is, final Consumer<String> lineHandler) {
		new Thread(() -> {
			try(final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
				String line;
				while((line = reader.readLine()) != null) {
					lineHandler.accept(line);
				}
				is.close();
			} catch (IOException e) {
				LOGGER.warn("got an I/O exception while reading software output with reason: {}", e.getMessage());
				return;
			}
		}).start();
	}

}
