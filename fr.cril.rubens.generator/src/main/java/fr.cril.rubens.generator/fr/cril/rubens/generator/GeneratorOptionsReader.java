package fr.cril.rubens.generator;

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
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.options.AppOptions;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * This class is used to parse the options of the command line interface and translate them into a configuration for the instance generator.
 * 
 * This class implements the singleton DP; call {@link GeneratorOptionsReader#getInstance()} to get its (only) instance.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class GeneratorOptionsReader extends AppOptions<GeneratorOptionsReader> {
	
	/** the default maximal depth for the generation tree */
	public static final int DEFAULT_MAX_DEPTH = 10;
	
	private static GeneratorOptionsReader instance = null; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorOptionsReader.class);
	
	private TestGeneratorFactory<Instance> factory;
	
	private File outputDirectory;
	
	private int maxDepth = DEFAULT_MAX_DEPTH;

	private GeneratorOptionsReader() {
		super(EGeneratorOption.values());
	}
	
	/**
	 * Gets the (only) {@link GeneratorOptionsReader} instance.
	 * 
	 * @return the {@link GeneratorOptionsReader} instance
	 */
	public static GeneratorOptionsReader getInstance() {
		if(instance == null) {
			instance = new GeneratorOptionsReader();
		}
		return instance;
	}
	
	@Override
	protected void reset() {
		this.factory = null;
		this.outputDirectory = null;
		this.maxDepth = DEFAULT_MAX_DEPTH;
	}
	
	@Override
	protected void checkOptionsRequirements() {
		if(this.factory == null) {
			LOGGER.error("no method set; use -m or --method");
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
			return;
		}
		if(this.outputDirectory == null) {
			LOGGER.error("no output directory set; use -o or --output");
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
	}
	
	protected String mandatoryOptionsToString() {
		return "-m method -o outputDirectory [other options]";
	}

	/**
	 * Prints the available generation methods and exits with a status of {@link GeneratorOptionsReader#STATUS_OPTION_EXIT_OK}.
	 * 
	 * The available methods are loaded through reflection.
	 */
	public void printMethodNamesAndExit() {
		if(LOGGER.isInfoEnabled()) {
			final TranslatorGeneratorReflector chk = TranslatorGeneratorReflector.getInstance();
			final SortedMap<String, SortedSet<String>> families = new TreeMap<>(this::pathSort);
			chk.familiesNames().stream().forEach(f -> families.computeIfAbsent(f, k -> new TreeSet<>()).addAll(chk.family(f)));
			for(final Entry<String, SortedSet<String>> entry: families.entrySet()) {
				LOGGER.info("available methods for family {}: {}", entry.getKey(), entry.getValue().stream().reduce((a,b) -> a+", "+b).orElseThrow());
			}
			final List<String> others = chk.withoutFamily().stream().sorted().collect(Collectors.toList());
			if(!others.isEmpty()) {
				LOGGER.info("available methods (no family): {}", chk.withoutFamily().stream().sorted().reduce((a,b) -> a+", "+b).orElseThrow());
			}
		}
		setMustExit(STATUS_OPTION_EXIT_OK);
	}
	
	private int pathSort(final String str1, final String str2) {
		final String[] s1 = str1.split("/");
		final String[] s2 = str2.split("/");
		int i=0;
		while(i < s1.length && i < s2.length) {
			final int cmp = s1[i].compareTo(s2[i]);
			if(cmp != 0) {
				return cmp;
			}
			i++;
		}
		return s1.length - s2.length;
	}
	
	/**
	 * Selects the generation methods, setting the one which name is provided.
	 * 
	 * If the name does not correspond to an available method, the application exits with a status of {@link GeneratorOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
	 * 
	 * @param name the name of the selected generation method
	 */
	@SuppressWarnings("unchecked")
	public void setMethod(final String name) {
		try {
			this.factory = TranslatorGeneratorReflector.getInstance().getClassInstance(name);
		} catch(IllegalArgumentException e) {
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
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

	/**
	 * Sets the maximal depth of the generation tree using the provided value.
	 * 
	 * The value is passed as a string; if it does not correspond to a valid depth (a strictly positive integer),
	 * the application exits with a status of {@link GeneratorOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
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
	 * Returns the {@link TestGeneratorFactory} instance to use as a generation method.
	 * 
	 * If it has not been set by the appropriate option, the value is <code>null</code>.
	 * 
	 * @return the {@link TestGeneratorFactory} instance to use as a generation method
	 */
	public TestGeneratorFactory<Instance> getFactory() {
		return this.factory;
	}
	
	/**
	 * Returns the output directory in which the generated instances must be stored.
	 * 
	 * If it has not been set by the appropriate option, the value is <code>null</code>.
	 * 
	 * @return the output directory in which the generated instances must be stored
	 */
	public File getOutputDirectory() {
		return this.outputDirectory;
	}
	
	/**
	 * Returns the maximal depth for the generation tree.
	 * 
	 * If it has not been set by the appropriate option, the value is {@link GeneratorOptionsReader#DEFAULT_MAX_DEPTH}.
	 * 
	 * @return the maximal depth for the generation tree
	 */
	public int getMaxDepth() {
		return this.maxDepth;
	}

	@Override
	protected GeneratorOptionsReader getThis() {
		return this;
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
