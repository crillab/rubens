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

import java.util.function.BiConsumer;

import org.apache.commons.cli.Options;

import fr.cril.rubens.options.IAppOption;
import fr.cril.rubens.options.OptionSpecs;

/**
 * An enumeration of all the supported command line interface options.
 *   
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public enum ECheckerOption implements IAppOption<CheckerOptionsReader> {

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
	
	/** set the binary under test */
	SET_EXEC("e", "exec", true, "set the software location", setExecLocation()),
	
	/** set the options dedicated to the checker */
	SET_CHECKER_OPTS("c", "checker-options", true, "set the checker options", setCheckerOptions()),
	
	/** display the license and exit */
	DISPLAY_LICENCE("g", "license", false, "display the license and exit", displayLicense());

	private final OptionSpecs specs;

	private final BiConsumer<CheckerOptionsReader, String> optionConsumer;

	/**
	 * Builds a command line interface option.
	 * 
	 * In addition to the parameters of the option itself (short name, long name, ...), a {@link BiConsumer} instance is required.
	 * When the option is read, the corresponding consumer is called and updates the configuration of the instance using the {@link CheckerOptionsReader} instance. 
	 * 
	 * @param opt the option short name
	 * @param longOpt the option long name
	 * @param hasArg a flag set to <code>true</code> iff the option has an argument (<em>i.e. a value on the CLI)</em>)
	 * @param description the textual description of the option
	 * @param consumer the consumer to call when the option is read
	 */
	private ECheckerOption(final String opt, final String longOpt, final boolean hasArg, final String description, final BiConsumer<CheckerOptionsReader, String> consumer) {
		this.specs = new OptionSpecs(opt, longOpt, hasArg, description);
		this.optionConsumer = consumer;
	}
	
	/**
	 * Builds and returns the {@link Options} object of Apache commons CLI library.
	 * 
	 * @return the {@link Options} object of Apache commons CLI library
	 */
	public static Options buildCliOptions() {
		final Options options = new Options();
		for(final ECheckerOption option : ECheckerOption.values()) {
			options.addOption(option.specs.getOpt(), option.specs.getLongOpt(), option.specs.hasArg(), option.specs.getDescription());
		}
		return options;
	}

	@Override
	public BiConsumer<CheckerOptionsReader, String> getOptionConsumer() {
		return this.optionConsumer;
	}

	private static BiConsumer<CheckerOptionsReader, String> printHelpAndExit() {
		return (o,s) -> o.printHelpAndExit();
	}

	private static BiConsumer<CheckerOptionsReader, String> printMethodNamesAndExit() {
		return (o, s) -> o.printMethodNamesAndExit();
	}
	
	private static BiConsumer<CheckerOptionsReader, String> setMethod() {
		return (o, s) -> o.setMethod(s);
	}
	
	private static BiConsumer<CheckerOptionsReader, String> setOutputDirectory() {
		return (o, s) -> o.setOutputDirectory(s);
	}
	
	private static BiConsumer<CheckerOptionsReader, String> setMaxDepth() {
		return (o, s) -> o.setMaxDepth(s);
	}
	
	private static BiConsumer<CheckerOptionsReader, String> setExecLocation() {
		return (o, s) -> o.setExecLocation(s);
	}
	
	private static BiConsumer<CheckerOptionsReader, String> setCheckerOptions() {
		return (o, s) -> o.setCheckerOptions(s);
	}
	
	private static BiConsumer<CheckerOptionsReader, String> displayLicense() {
		return (o, s) -> o.printLicenseAndExit();
	}
	
	@Override
	public OptionSpecs getSpecs() {
		return this.specs;
	}

}
