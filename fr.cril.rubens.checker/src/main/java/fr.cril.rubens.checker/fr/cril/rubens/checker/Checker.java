package fr.cril.rubens.checker;

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
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.utils.ASoftwareExecutor;
import fr.cril.rubens.utils.GNUGPL3;
import fr.cril.rubens.utils.SoftwareExecutorResult;

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
	
	private int ignCount = 0;
	
	private final Object ignCountLock = new Object();
	
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
		GNUGPL3.logWelcomeMessage(LOGGER);
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
		if(checkerOptions.mustExit()) {
			if(this.statusCode != 0) {
				LOGGER.error("an error occurred while parsing this generator parameters; ignoring the checking process");
			}
			return;
		}
		final Map<String, CheckerFactory<Instance>> factories = this.checkerOptions.getFactories();
		final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(final Entry<String, CheckerFactory<Instance>> factoryEntry : factories.entrySet()) {
			final CheckerFactory<Instance> factory = factoryEntry.getValue();
			applyCheckerOptions(this.checkerOptions.getCheckerOptions(), factory);
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
		LOGGER.info("ignored {} instances.", this.ignCount);
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
		if(factory.ignoreInstance(instance)) {
			synchronized (this.ignCountLock) {
				this.ignCount++;
			}
			return;
		}
		final ASoftwareExecutor<Instance> executor = factory.newExecutor(Paths.get(this.checkerOptions.getExecLocation()));
		threadPool.submit(() -> {
			CheckResult checkResult;
			try {
				final SoftwareExecutorResult result = executor.exec(instance);
				checkResult = factory.checkSoftwareOutput(instance, result.getStdout());
			} catch(Exception e) {
				LOGGER.error("an unexpected exception occurred for instance {} with the message \"{}\"", instance, e.getMessage());
				checkResult = CheckResult.newError("an unexpected exception occurred");
			}
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
	
	/**
	 * Returns the number of checks that have been realized.
	 * 
	 * @return the number of checks that have been realized
	 */
	public int getCheckCount() {
		return this.checkCount;
	}
	
	/**
	 * Returns the number of errors found during the checking process.
	 * 
	 * @return the number of errors found during the checking process
	 */
	public int getErrorCount() {
		return this.errorCount;
	}
	
	/**
	 * Returns the number of ignored instances during the checking process.
	 * 
	 * @return the number of ignored instances during the checking process
	 */
	public int getIgnoredCount() {
		return this.ignCount;
	}
	
	/**
	 * Returns the app status code.
	 * It is equals to zero if the process exited normally, even if the software under test has errors. 
	 * 
	 * @return the app status code
	 */
	public int getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * Applies options on a checker factory.
	 * 
	 * If some options are not recognized, some warnings may be logged.
	 * 
	 * @param options the options
	 * @param factory the checker factory
	 */
	private void applyCheckerOptions(final String options, final CheckerFactory<Instance> factory) {
		final Map<String, MethodOption> optionMap = factory.getOptions().stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		final String[] opts = options.split(",");
		for(final String opt: opts) {
			if(opt.trim().isEmpty()) {
				continue;
			}
			int indexOf = opt.indexOf('=');
			if(indexOf != -1 && optionMap.containsKey(opt.substring(0, indexOf))) {
				optionMap.get(opt.substring(0, indexOf)).apply(opt.substring(1+indexOf));
			} else {
				LOGGER.warn("invalid checker option: {}", opt);
			}
		}
	}
	
}
