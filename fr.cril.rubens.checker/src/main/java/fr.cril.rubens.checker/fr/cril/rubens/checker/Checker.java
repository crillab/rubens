package fr.cril.rubens.checker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.TestGenerator;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;

/**
 * The main class of the checker module, acting as entry point for the binary.
 * 
 * This class is responsible of the generation of the test instances, their execution by the software under test, and the checking of the result.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class Checker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
	
	private int statusCode;
	
	private int checkCount = 0;
	
	private final Object checkCountLock = new Object();
	
	private int errorCount = 0;
	
	private final Object errorCountLock = new Object();
	
	private final CheckerOptionsReader checkerOptions;

	private boolean cleanedOldFiles = false;
	
	/**
	 * Application entry point.
	 * 
	 * See {@link ECheckerOption} for a list of the supported options.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {
		final Checker checker = new Checker(args);
		checker.check();
		System.exit(checker.statusCode);
	}
	
	/**
	 * Builds a new instance of the checker given the command line arguments.
	 * 
	 * @param args the command line arguments
	 */
	public Checker(final String[] args) {
		this.checkerOptions = CheckerOptionsReader.getInstance();
		checkerOptions.loadOptions(args);
		if(checkerOptions.mustExit()) {
			this.statusCode = checkerOptions.exitStatus();
		}
	}
	
	/**
	 * Launches the global checking process.
	 */
	public void check() {
		final long startTime = System.currentTimeMillis();
		if(this.statusCode != 0) {
			LOGGER.error("an error occurred while parsing this generator parameters; ignoring the checking process");
			return;
		}
		final Map<String, CheckerFactory<Instance>> factories = this.checkerOptions.getFactories();
		final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(final Entry<String, CheckerFactory<Instance>> factoryEntry : factories.entrySet()) {
			final CheckerFactory<Instance> factory = factoryEntry.getValue();
			factory.setOptions(this.checkerOptions.getCheckerOptions());
			final TestGenerator<Instance> generator = new TestGenerator<>(factory.newTestGenerator());
			final String factoryName = factoryEntry.getKey();
			LOGGER.info("checking {}", factoryName);
			generator.computeToDepth(this.checkerOptions.getMaxDepth(), i -> this.checkInstance(threadPool, factory, factoryName, i));
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.error("got an error while waiting for checking threads", e);
		}
		final Supplier<String> strTimeSupplier = () -> String.format("%.3f", (System.currentTimeMillis() - startTime)/1000f);
		LOGGER.info("checked {} instances in {}s", this.checkCount, strTimeSupplier.get());
		LOGGER.info("found {} errors.", this.errorCount);
	}
	
	/**
	 * Given a checking factory, executes the software under test on the provided instance and checks the result.
	 * This method uses a thread pool to allow parallelization of instance checking.
	 * 
	 * @param threadPool the thread pool
	 * @param factory the factory
	 * @param factoryName the name of the factory under consideration
	 * @param instance the instance
	 */
	private void checkInstance(final ExecutorService threadPool, final CheckerFactory<Instance> factory, final String factoryName, final Instance instance) {
		threadPool.submit(() -> {
			final String softwareOutput = factory.execSoftware(this.checkerOptions.getExecLocation(), instance);
			final CheckResult checkResult = factory.checkSoftwareOutput(instance, softwareOutput);
			if(!checkResult.isSuccessful()) {
				synchronized (this.errorCountLock) {
					this.errorCount++;
					LOGGER.error("{} error ({}) for instance {}: {}.", factoryName, this.errorCount, instance, checkResult.getExplanation());
					if(this.checkerOptions.getOutputDirectory() != null) {
						outputInstance(factoryName, instance);
					}
				}
			}
			synchronized (this.checkCountLock) {
				this.checkCount++;
			}
		});
	}
	
	private void outputInstance(final String factoryName, final Instance instance) {
		if(this.statusCode != 0) {
			return;
		}
		final Collection<String> extensions = instance.getFileExtensions();
		final File outputDirectory = this.checkerOptions.getOutputDirectory();
		cleanOldFiles(outputDirectory, extensions);
		try {
			for(final String ext : extensions) {
				instance.write(ext, new FileOutputStream(new File(outputDirectory, factoryName+"-"+this.errorCount+ext)));
			}
		} catch (IOException e) {
			this.statusCode = 1;
		}
	}
	
	private void cleanOldFiles(final File outputDirectory, final Collection<String> extensions) {
		if(!this.cleanedOldFiles) {
			this.cleanedOldFiles = true;
			final File[] folderFiles = outputDirectory.listFiles(File::isFile);
			if(folderFiles == null) {
				LOGGER.warn("cannot access directory {} for cleaning", outputDirectory);
				return;
			}
			for(final Path path : Arrays.stream(folderFiles).map(File::toURI).map(Paths::get).collect(Collectors.toList())) {
				if(extensions.stream().anyMatch(path.toString()::endsWith)) {
					try {
						Files.delete(path);
					} catch (IOException e) {
						LOGGER.warn("cannot clean existing file {}; reason is \"{}\"", path.toAbsolutePath(), e.getMessage());
					}
				}
			}
		}
	}
	
}
