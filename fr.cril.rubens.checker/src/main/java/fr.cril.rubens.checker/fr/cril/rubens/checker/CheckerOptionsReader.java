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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;

import fr.cril.rubens.checker.utils.PathComparator;
import fr.cril.rubens.options.AppOptions;
import fr.cril.rubens.reflection.CheckerFactoryCollectionReflector;
import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryCollection;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.utils.LoggerHelper;

/**
 * This class is used to parse the options of the command line interface and translate them into a configuration for the instance generator.
 * 
 * This class implements the singleton DP; call {@link CheckerOptionsReader#getInstance()} to get its (only) instance.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class CheckerOptionsReader extends AppOptions<CheckerOptionsReader> {
	
	/** the default maximal depth for the generation tree */
	public static final int DEFAULT_MAX_DEPTH = 10;
	
	private static CheckerOptionsReader instance = null; 
	
	private static final Logger LOGGER = LoggerHelper.getInstance().getLogger();
	
	private final Map<String, CheckerFactory<Instance>> factories = new LinkedHashMap<>();
	
	private File outputDirectory;
	
	private int maxDepth = DEFAULT_MAX_DEPTH;
	
	private String execLocation;
	
	private String checkerOptions = "";

	private CheckerOptionsReader() {
		super(ECheckerOption.values());
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
	
	protected void reset() {
		this.factories.clear();
		this.maxDepth = DEFAULT_MAX_DEPTH;
	}
	
	protected void checkOptionsRequirements() {
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
	
	protected String mandatoryOptionsToString() {
		return "-m method -e execLocation [other options]";
	}

	/**
	 * Prints the available generation methods and exits with a status of {@link CheckerOptionsReader#STATUS_OPTION_EXIT_OK}.
	 * 
	 * The available methods are loaded through reflection.
	 */
	public void printMethodNamesAndExit() {
		if(LOGGER.isInfoEnabled()) {
			final CheckerFactoryReflector chkRefl = CheckerFactoryReflector.getInstance();
			final CheckerFactoryCollectionReflector colChkRefl = CheckerFactoryCollectionReflector.getInstance();
			final SortedMap<String, SortedSet<String>> families = new TreeMap<>(new PathComparator());
			chkRefl.familiesNames().stream().forEach(f -> families.computeIfAbsent(f, k -> new TreeSet<>()).addAll(chkRefl.family(f)));
			colChkRefl.familiesNames().stream().forEach(f -> families.computeIfAbsent(f, k -> new TreeSet<>()).addAll(colChkRefl.family(f)));
			for(final Entry<String, SortedSet<String>> entry: families.entrySet()) {
				LOGGER.info("available methods for family {}: {}", entry.getKey(), entry.getValue().stream().reduce((a,b) -> a+", "+b).orElseThrow());
			}
			final List<String> others = Stream.concat(chkRefl.withoutFamily().stream(), colChkRefl.withoutFamily().stream()).sorted().collect(Collectors.toList());
			if(!others.isEmpty()) {
				LOGGER.info("available methods (no family): {}", others.stream().reduce((a,b) -> a+", "+b).orElseThrow());
			}
		}
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
	 * the application exits with a status of {@link CheckerOptionsReader#STATUS_OPTIONS_EXIT_ERROR}.
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
		final Path path = Paths.get(location);
		if(!Files.isRegularFile(path) || !Files.isExecutable(path)) {
			LOGGER.error("expected a path to an executable regular file, got \"{}\"", location);
			setMustExit(STATUS_OPTIONS_EXIT_ERROR);
		}
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
	 * Sets the options dedicated to the checker itself.
	 * 
	 * These options must be contained in a single string.
	 * There is no other formatting requirements; the checker is responsible of the use of this string.
	 * 
	 * @param options the checker options
	 */
	public void setCheckerOptions(final String options) {
		this.checkerOptions = options;
	}
	
	/**
	 * Returns the options dedicated to the checker.
	 * 
	 * @return the options dedicated to the checker
	 */
	public String getCheckerOptions() {
		return this.checkerOptions;
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
		return new LinkedHashMap<>(this.factories);
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

	@Override
	protected CheckerOptionsReader getThis() {
		return this;
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
