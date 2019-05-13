package fr.cril.rubens.generator;

import java.util.function.BiConsumer;

import org.apache.commons.cli.Options;

import fr.cril.rubens.options.IAppOption;

/**
 * An enumeration of all the supported command line interface options.
 *   
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public enum EGeneratorOption implements IAppOption<GeneratorOptionsReader> {

	/** display help and exit */
	DISPLAY_HELP("h", "help", false, "display help and exit", printHelpAndExit()),
	
	/** display the available methods (<em>i.e.</em> generation algorithms) and exit */ 
	METHOD_LIST("l", "list", false, "display method names and exit", printMethodNamesAndExit()),
	
	/** set the generation method */
	SET_METHOD("m", "method", true, "set the generation method", setMethod()),
	
	/** set the output directory */
	SET_OUTPUT("o", "output", true, "set the generation output directory", setOutputDirectory()),
	
	/** set the generation tree max depth */
	SET_DEPTH("d", "depth", true, "set the computation tree max depth", setMaxDepth()),
	
	/** display the license and exit */
	DISPLAY_LICENCE("g", "license", false, "display the license and exit", displayLicense());

	private final String opt;

	private final String longOpt;

	private final boolean hasArg;

	private final String description;

	private final BiConsumer<GeneratorOptionsReader, String> optionConsumer;

	/**
	 * Builds a command line interface option.
	 * 
	 * In addition to the parameters of the option itself (short name, long name, ...), a {@link BiConsumer} instance is required.
	 * When the option is read, the corresponding consumer is called and updates the configuration of the instance using the {@link GeneratorOptionsReader} instance. 
	 * 
	 * @param opt the option short name
	 * @param longOpt the option long name
	 * @param hasArg a flag set to <code>true</code> iff the option has an argument (<em>i.e. a value on the CLI)</em>)
	 * @param description the textual description of the option
	 * @param consumer the consumer to call when the option is read
	 */
	private EGeneratorOption(final String opt, final String longOpt, final boolean hasArg, final String description, final BiConsumer<GeneratorOptionsReader, String> consumer) {
		this.opt = opt;
		this.longOpt = longOpt;
		this.hasArg = hasArg;
		this.description = description;
		this.optionConsumer = consumer;
	}
	
	/**
	 * Builds and returns the {@link Options} object of Apache commons CLI library.
	 * 
	 * @return the {@link Options} object of Apache commons CLI library
	 */
	public static Options buildCliOptions() {
		final Options options = new Options();
		for(final EGeneratorOption option : EGeneratorOption.values()) {
			options.addOption(option.opt, option.longOpt, option.hasArg, option.description);
		}
		return options;
	}

	@Override
	public String getOpt() {
		return this.opt;
	}
	
	@Override
	public String getLongOpt() {
		return this.longOpt;
	}
	
	@Override
	public boolean hasArg() {
		return this.hasArg;
	}
	
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public BiConsumer<GeneratorOptionsReader, String> getOptionConsumer() {
		return this.optionConsumer;
	}

	private static BiConsumer<GeneratorOptionsReader, String> printHelpAndExit() {
		return (o,s) -> o.printHelpAndExit();
	}

	private static BiConsumer<GeneratorOptionsReader, String> printMethodNamesAndExit() {
		return (o, s) -> o.printMethodNamesAndExit();
	}
	
	private static BiConsumer<GeneratorOptionsReader, String> setMethod() {
		return (o, s) -> o.setMethod(s);
	}
	
	private static BiConsumer<GeneratorOptionsReader, String> setOutputDirectory() {
		return (o, s) -> o.setOutputDirectory(s);
	}
	
	private static BiConsumer<GeneratorOptionsReader, String> setMaxDepth() {
		return (o, s) -> o.setMaxDepth(s);
	}
	
	private static BiConsumer<GeneratorOptionsReader, String> displayLicense() {
		return (o, s) -> o.printLicenseAndExit();
	}

}
