package fr.cril.rubens.checker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.reflection.CheckerFactoryCollectionReflector;
import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryCollection;
import fr.cril.rubens.specs.Instance;

/**
 * This class is used to parse the options of the command line interface and translate them into a configuration for the instance generator.
 * 
 * This class implements the singleton DP; call {@link CheckerOptionsReader#getInstance()} to get its (only) instance.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class CheckerOptionsReader {
	
	/** the default maximal depth for the generation tree */
	public static final int DEFAULT_MAX_DEPTH = 10;
	
	/** the status returned when the program exits normally after reading some options */
	public static final int STATUS_OPTION_EXIT_OK = 1;
	
	/** the status returned when the program exits after encountering an error while checking CLI the options */
	public static final int STATUS_OPTIONS_EXIT_ERROR = 2;
	
	private static CheckerOptionsReader instance = null; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckerOptionsReader.class);
	
	private static final int HELP_FORMATTER_MAX_WIDTH = 80;
	
	private Options options;
	
	private boolean mustExit = false;

	private int status;

	private final Map<String, CheckerFactory<Instance>> factories = new HashMap<>();
	
	private File outputDirectory;
	
	private int maxDepth = DEFAULT_MAX_DEPTH;
	
	private String execLocation;

	private CheckerOptionsReader() {
		// nothing to do
	}
	
	/**
	 * Gets the (only) {@link CheckerOptionsReader} instance.
	 * 
	 * @return the {@link CheckerOptionsReader} instance
	 */
	public static CheckerOptionsReader getInstance() {
		if(instance == null) {
			instance = new CheckerOptionsReader();
		}
		return instance;
	}
	
	/**
	 * Loads the options from the CLI arguments.
	 * 
	 * If an option implying a normal termination of the application after options reading is found,
	 * the application exits with a status of {@link CheckerOptionsReader#STATUS_OPTION_EXIT_OK}.
	 * 
	 * If an error occurs while loading the options,
	 * the application exits with a status of {@link CheckerOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * If both exit status should be returned, it is not defined which one is returned.
	 * 
	 * @param args the CLI arguments
	 */
	public void loadOptions(final String[] args) {
		reset();
		this.options = ECheckerOption.buildCliOptions();
		final CommandLineParser parser = new DefaultParser();
		try {
			final CommandLine commandLine = parser.parse(options, args);
			applyOptions(commandLine);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
	}
	
	private void reset() {
		this.options = null;
		this.mustExit = false;
		this.status = 0;
		this.factories.clear();
		this.maxDepth = DEFAULT_MAX_DEPTH;
	}
	
	private void applyOptions(final CommandLine cmdl) {
		for(final ECheckerOption option : ECheckerOption.values()) {
			if(cmdl.hasOption(option.getOpt())) {
				final String value = cmdl.getOptionValue(option.getOpt());
				option.getOptionConsumer().accept(this, value);
			}
			if(this.mustExit) {
				break;
			}
		}
		if(!this.mustExit) {
			checkOptionsRequirements();
		}
	}
	
	private void checkOptionsRequirements() {
		if(this.factories.isEmpty()) {
			LOGGER.error("no method set; use -m or --method");
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
			return;
		}
		if(this.execLocation == null) {
			LOGGER.error("no exec location; use -e or --exec");
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
	}

	/**
	 * Prints the help and exits with a status of {@link CheckerOptionsReader#STATUS_OPTION_EXIT_OK}.
	 */
	public void printHelpAndExit() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final PrintWriter writer = new PrintWriter(outputStream);
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(writer, HELP_FORMATTER_MAX_WIDTH, "-m method -o outputDirectory [other options]",
				"RUBENS - CRIL - Univ. Artois & CNRS", this.options, 4, 4, "");
		writer.flush();
		final String msg = new String(outputStream.toByteArray());
		LOGGER.info(msg);
		setMustExit(STATUS_OPTION_EXIT_OK);
	}
	
	private void setMustExit(final int status) {
		LOGGER.debug("exit requested with status {}", status);
		this.mustExit = true;
		this.status = status;
	}
	
	/**
	 * Prints the available generation methods and exits with a status of {@link CheckerOptionsReader#STATUS_OPTION_EXIT_OK}.
	 * 
	 * The available methods are loaded through reflection.
	 */
	public void printMethodNamesAndExit() {
		final Stream<String> checkerFactories = CheckerFactoryReflector.getInstance().classesNames().stream();
		final Stream<String> checkerFactoryCollections = CheckerFactoryCollectionReflector.getInstance().classesNames().stream();
		final String methods = Stream.concat(checkerFactories, checkerFactoryCollections).sorted().reduce((a,b) -> a+", "+b).orElse("<none>");
		LOGGER.info("available methods: {}", methods);
		setMustExit(STATUS_OPTION_EXIT_OK);
	}
	
	/**
	 * Selects the generation methods, setting the one which name is provided.
	 * 
	 * If the name does not correspond to an available method, the application exits with a status of {@link CheckerOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * @param name the name of the selected generation method
	 */
	@SuppressWarnings("unchecked")
	public void setMethod(final String name) {
		final CheckerFactoryCollectionReflector checkerFactoryCollectionReflector = CheckerFactoryCollectionReflector.getInstance();
		final Collection<String> checkerFactoryCollections = checkerFactoryCollectionReflector.classesNames();
		if(CheckerFactoryReflector.getInstance().classesNames().contains(name)) {
			if(checkerFactoryCollections.contains(name)) {
				LOGGER.error("multiple declaraction of method {}", name);
				setMustExit(STATUS_OPTIONS_EXIT_ERROR);
				return;
			}
			this.factories.put(name, CheckerFactoryReflector.getInstance().getClassInstance(name));
			return;
		}
		if(!checkerFactoryCollections.contains(name)) {
			LOGGER.error("no method {}", name);
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
			return;
		}
		final CheckerFactoryCollection<Instance> collection = checkerFactoryCollectionReflector.getClassInstance(name);
		final Set<String> collectionNames = collection.getNames();
		collectionNames.stream().forEach(n -> this.factories.put(n, collection.getFactory(n)));
	}
	
	/**
	 * Sets the output directory as the one described by the provided path.
	 * 
	 * If such path cannot be used as a directory to store instances,
	 * the application exits with a status of {@link GeneratorOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * @param path the path of the output directory to set
	 */
	public void setOutputDirectory(final String path) {
		try {
			this.outputDirectory = getOrCreateOutputDirectory(path);
		} catch(IllegalArgumentException e) {
			LOGGER.error(e.getMessage());
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
	}
	
	private File getOrCreateOutputDirectory(final String path) {
		final File file = new File(path);
		if(file.exists()) {
			if(!file.isDirectory()) {
				throw new IllegalArgumentException(path+" must be a directory");
			}
		} else {
			if(!file.mkdirs()) {
				throw new IllegalArgumentException("cannot create directory "+path);
			}
		}
		if(!file.canRead() || !file.canWrite()) {
			throw new IllegalArgumentException(path+" must be a directory with read and write access");
		}
		return file;
	}
	
	/**
	 * Sets the maximal depth of the generation tree using the provided value.
	 * 
	 * The value is passed as a string; if it does not correspond to a valid depth (a strictly positive integer),
	 * the application exits with a status of {@link CheckerOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * @param value the maximal depth value
	 */
	public void setMaxDepth(final String value) {
		int depth = -1;
		final String errorMsg = "wrong value for argument depth: expected a strictly positive integer, got {}";
		try {
			depth = Integer.valueOf(value);
		} catch(NumberFormatException e) {
			LOGGER.error(errorMsg, value);
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
		if(depth < 1) {
			LOGGER.error(errorMsg, value);
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
		this.maxDepth = depth;
	}
	
	/**
	 * Sets the location of the software under test.
	 * 
	 * @param location the location
	 */
	public void setExecLocation(final String location) {
		this.execLocation = location;
	}
	
	/**
	 * Gets the location of the software under test.
	 * 
	 * @return the location of the software under test
	 */
	public String getExecLocation() {
		return this.execLocation;
	}
	
	/**
	 * Returns <code>true</code> iff the application must exit.
	 * 
	 * The exit flag is set by some CLI options or when an error occurs.
	 * 
	 * @return <code>true</code> iff the application must exit
	 */
	public boolean mustExit() {
		return this.mustExit;
	}
	
	/**
	 * Returns the exit status that must be returned when exiting.
	 * 
	 * The behavior is undefined if the application may not exit;
	 * you should call {@link CheckerOptionsReader#mustExit} before calling this method.
	 * 
	 * @return the exit status that must be returned when exiting
	 */
	public int exitStatus() {
		return this.status;
	}
	
	/**
	 * Returns the {@link CheckerFactory} instances to use as as checking method.
	 * Instances are returned as a mapping from the method names to the factories themselves.
	 * 
	 * If it has not been set by the appropriate option, the map is empty.
	 * 
	 * @return the {@link CheckerFactory} instances
	 */
	public Map<String, CheckerFactory<Instance>> getFactories() {
		return Collections.unmodifiableMap(this.factories);
	}
	
	/**
	 * Returns the output directory in which the failed instances must be stored.
	 * 
	 * If it has not been set by the appropriate option, the value is <code>null</code>.
	 * 
	 * @return the output directory in which the failed instances must be stored
	 */
	public File getOutputDirectory() {
		return this.outputDirectory;
	}
	
	/**
	 * Returns the maximal depth for the generation tree.
	 * 
	 * If it has not been set by the appropriate option, the value is {@link CheckerOptionsReader#DEFAULT_MAX_DEPTH}.
	 * 
	 * @return the maximal depth for the generation tree
	 */
	public int getMaxDepth() {
		return this.maxDepth;
	}

}
