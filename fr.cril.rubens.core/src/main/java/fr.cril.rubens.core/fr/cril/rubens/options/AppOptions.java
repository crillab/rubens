package fr.cril.rubens.options;

/*-
 * #%L
 * RUBENS core API
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.utils.GNUGPL3;

/**
 * Common code used by RUBENS applications to handle their command line options (instances of {@link IAppOption}).
 * 
 * The parameter type of underlying {@link IAppOption} instances must be passed to this class. 
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the parameter type of underlying {@link IAppOption} instances
 */
public abstract class AppOptions<T> {
	
	/** the status returned when the program exits normally after reading some options */
	public static final int STATUS_OPTION_EXIT_OK = 0;
	
	/** the status returned when the program exits after encountering an error while checking CLI the options */
	public static final int STATUS_OPTIONS_EXIT_ERROR = 1;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppOptions.class);
	
	private static final int HELP_FORMATTER_MAX_WIDTH = 80;
	
	private final IAppOption<T>[] appOpts;
	
	private boolean mustExit = false;

	private int status;

	/**
	 * Builds an instance of this class given the set of command line options under consideration.
	 * 
	 * @param options the set of command line options under consideration
	 */
	protected AppOptions(final IAppOption<T>[] options) {
		this.appOpts = options;
	}
	
	/**
	 * Loads the options from the CLI arguments.
	 * 
	 * If an option implying a normal termination of the application after options reading is found,
	 * the application exits with a status of {@link AppOptions#STATUS_OPTION_EXIT_OK}.
	 * 
	 * If an error occurs while loading the options,
	 * the application exits with a status of {@link AppOptions#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * If both exit status should be returned, it is not defined which one is returned.
	 * 
	 * @param args the CLI arguments
	 */
	public void loadOptions(final String[] args) {
		this.mustExit = false;
		this.status = 0;
		reset();
		final CommandLineParser parser = new DefaultParser();
		try {
			final CommandLine commandLine = parser.parse(buildCliOptions(), args);
			applyOptions(commandLine);
		} catch (ParseException e) {
			getLogger().error(e.getMessage());
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
	}
	
	/**
	 * Builds and returns the {@link Options} object of Apache commons CLI library.
	 * 
	 * @return the {@link Options} object of Apache commons CLI library
	 */
	private Options buildCliOptions() {
		final Options options = new Options();
		for(final IAppOption<T> option : this.appOpts) {
			options.addOption(option.getOpt(), option.getLongOpt(), option.hasArg(), option.getDescription());
		}
		return options;
	}
	
	private void applyOptions(final CommandLine cmdl) {
		for(final IAppOption<T> option : this.appOpts) {
			if(cmdl.hasOption(option.getOpt())) {
				final String value = cmdl.getOptionValue(option.getOpt());
				option.getOptionConsumer().accept(getThis(), value);
			}
			if(this.mustExit) {
				break;
			}
		}
		if(!this.mustExit) {
			checkOptionsRequirements();
		}
	}
	
	/**
	 * Returns the instance of the class extending this instance of {@link AppOptions}.
	 * 
	 * @return the instance of the class extending this instance of {@link AppOptions}
	 */
	protected abstract T getThis();
	
	/**
	 * Checks if implementors specific requirements about options are met.
	 * 
	 * In case there aren't, the implementor must call {@link AppOptions#setMustExit(int)} with a non-zero exit code.
	 */
	protected abstract void checkOptionsRequirements();
	
	/**
	 * Tells the application it must exit with the provided status.
	 * 
	 * @param status the status the application must exit with
	 */
	protected void setMustExit(final int status) {
		getLogger().debug("exit requested with status {}", status);
		this.mustExit = true;
		this.status = status;
	}
	
	/**
	 * Prints the help and exits with a status of {@link AppOptions#STATUS_OPTION_EXIT_OK}.
	 */
	public void printHelpAndExit() {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final PrintWriter writer = new PrintWriter(outputStream);
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(writer, HELP_FORMATTER_MAX_WIDTH, mandatoryOptionsToString(),
				"RUBENS - CRIL - Artois University & CNRS", buildCliOptions(), 4, 4, "");
		writer.flush();
		final String msg = new String(outputStream.toByteArray());
		getLogger().info(msg);
		setMustExit(STATUS_OPTION_EXIT_OK);
	}
	
	public void printLicenseAndExit() {
		GNUGPL3.logTermsAndConditions(LOGGER);
		setMustExit(STATUS_OPTION_EXIT_OK);
	}
	
	/**
	 * Returns a {@link String} describing the mandatory arguments of the application.
	 * 
	 * @return a {@link String} describing the mandatory arguments of the application
	 */
	protected abstract String mandatoryOptionsToString();
	
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
	 * you should call {@link AppOptions#mustExit} before calling this method.
	 * 
	 * @return the exit status that must be returned when exiting
	 */
	public int exitStatus() {
		return this.status;
	}
	
	/**
	 * Resets the options of the implementor.
	 */
	protected abstract void reset();
	
	/**
	 * Returns the logger to use.
	 * 
	 * @return the logger to use
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Utility method used to create/check an output directory given its path.
	 * 
	 * If the directory does not exist, the app must be able to create it.
	 * If a file exists with the same name, it must be a directory with write access.
	 * 
	 * In case one of this condition is not met, an {@link IllegalArgumentException} is thrown.
	 * 
	 * In case of success, the corresponding {@link File} object is returned.
	 * 
	 * @param path the path to the directory
	 * @return the corresponding {@link File} object
	 */
	protected File getOrCreateOutputDirectory(final String path) {
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

}
