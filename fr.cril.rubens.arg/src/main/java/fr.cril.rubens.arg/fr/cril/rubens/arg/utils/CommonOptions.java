package fr.cril.rubens.arg.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.core.Option;

/**
 * A class used to generate AF method common options.
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
	public List<Option> getOptions(final ArgumentationFrameworkCheckerFactory<?> checkerFactory) {
		return Arrays.stream(LocalOption.values()).map(o -> o.toOption(checkerFactory)).collect(Collectors.toUnmodifiableList());
	}

	private enum LocalOption {

		OUTPUT_FORMAT("outputFormat", "the output format used in the competition (ICCMA17 or ICCMA19)",
				(value, checker) -> checker
						.setOutputFormat(SolverOutputDecoderFactory.getInstanceByName(value).getDecoderInstance()));

		private final String name;

		private final String description;

		private final BiConsumer<String, ArgumentationFrameworkCheckerFactory<?>> applier;

		private LocalOption(final String name, final String description, final BiConsumer<String, ArgumentationFrameworkCheckerFactory<?>> applier) {
			this.name = name;
			this.description = description;
			this.applier = applier;
		}

		private Option toOption(final ArgumentationFrameworkCheckerFactory<?> factory) {
			return new Option(this.name, this.description, value -> this.applier.accept(value, factory));
		}
	}

}
