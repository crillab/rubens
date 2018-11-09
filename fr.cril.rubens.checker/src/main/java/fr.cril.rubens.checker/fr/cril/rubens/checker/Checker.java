package fr.cril.rubens.checker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
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
	
	private int checkCount;
	
	private int errorCount;
	
	private final CheckerOptionsReader checkerOptions;

	private CheckerFactory<Instance> checkerFactory;
	
	private int instanceCount;
	
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
		this.checkerFactory = this.checkerOptions.getFactory();
		final TestGenerator<Instance> generator = new TestGenerator<>(this.checkerFactory.newTestGenerator());
		generator.computeToDepth(this.checkerOptions.getMaxDepth(), this::checkInstance);
		final Supplier<String> strTimeSupplier = () -> String.format("%.3f", (System.currentTimeMillis() - startTime)/1000f);
		LOGGER.info("checked {} instances in {}s", this.checkCount, strTimeSupplier.get());
		LOGGER.info("found {} errors.", this.errorCount);
	}
	
	/**
	 * Executes the software under test on the provided instance and checks the result.
	 * 
	 * @param instance the instance
	 */
	public void checkInstance(final Instance instance) {
		final String softwareOutput = this.checkerFactory.execSoftware(this.checkerOptions.getExecLocation(), instance);
		final CheckResult checkResult = this.checkerFactory.checkSoftwareOutput(instance, softwareOutput);
		if(!checkResult.isSuccessful()) {
			this.errorCount++;
			LOGGER.error("error ({}) for instance {}: {}.", this.instanceCount, instance, checkResult.getExplanation());
			if(this.checkerOptions.getOutputDirectory() != null) {
				outputInstance(instance);
			}
		}
		this.checkCount++;
	}
	
	private void outputInstance(final Instance instance) {
		if(this.statusCode != 0) {
			return;
		}
		final Collection<String> extensions = instance.getFileExtensions();
		final File outputDirectory = this.checkerOptions.getOutputDirectory();
		cleanOldFiles(outputDirectory, extensions);
		try {
			for(final String ext : extensions) {
				instance.write(ext, new FileOutputStream(new File(outputDirectory, instanceCount+ext)));
			}
			this.instanceCount++;
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
