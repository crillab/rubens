package fr.cril.rubens.cnf.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import fr.cril.rubens.cnf.core.ASatCheckerFactory;
import fr.cril.rubens.core.Option;

/**
 * A class used to generate SAT based checkers common options.
 * 
 * This class implements the singleton design pattern; call {@link CommonOptions#getInstance()} to get its instance.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class CommonOptions {
	
	private static CommonOptions instance = null;
	
	/**
	 * Returns the (only) instance of this class.
	 * 
	 * @return the (only) instance of this class
	 */
	public static synchronized CommonOptions getInstance() {
		if(instance == null) {
			instance = new CommonOptions();
		}
		return instance;
	}
	
	private CommonOptions() {
		// nothing to do here
	}
	
	/**
	 * Builds the common options for a checker factory.
	 * 
	 * @param checkerFactory the checker factory
	 * @return the common options for the factory
	 */
	public List<Option> getOptions(final ASatCheckerFactory<?> checkerFactory) {
		return Arrays.stream(LocalOption.values()).map(o -> o.toOption(checkerFactory)).collect(Collectors.toUnmodifiableList());
	}

	private enum LocalOption {

		IGN_UNSAT("ignoreUnsat", "ignore checks for unsatisfiable instances (on or off - default is off)",
				(value, checker) -> checker.ignoreUnsat(strToBool(value)));

		private final String name;

		private final String description;

		private final BiConsumer<String, ASatCheckerFactory<?>> applier;

		private LocalOption(final String name, final String description, final BiConsumer<String, ASatCheckerFactory<?>> applier) {
			this.name = name;
			this.description = description;
			this.applier = applier;
		}

		private Option toOption(final ASatCheckerFactory<?> factory) {
			return new Option(this.name, this.description, value -> this.applier.accept(value, factory));
		}
		
	}
	
	/**
	 * Converts a string value given by <code>on</code> or <code>off<code> into the corresponding Boolean value.
	 * 
	 *  If another value is given, an {@link IllegalArgumentException} is thrown.
	 *  
	 * @param value the string value
	 * @return the corresponding Boolean value
	 */
	public static boolean strToBool(final String value) {
		if("on".equalsIgnoreCase(value)) {
			return true;
		} else if("off".equalsIgnoreCase(value)) {
			return false;
		}
		throw new IllegalArgumentException("\""+value+"\" cannot be cast into a Boolean value (use \"on\" or \"off\"");
	}

}
