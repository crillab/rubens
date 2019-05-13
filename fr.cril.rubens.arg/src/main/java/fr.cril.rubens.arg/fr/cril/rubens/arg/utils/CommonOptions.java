package fr.cril.rubens.arg.utils;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
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

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.options.MethodOption;

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
	public List<MethodOption> getOptions(final ArgumentationFrameworkCheckerFactory<?> checkerFactory) {
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

		private MethodOption toOption(final ArgumentationFrameworkCheckerFactory<?> factory) {
			return new MethodOption(this.name, this.description, value -> this.applier.accept(value, factory));
		}
	}

}
