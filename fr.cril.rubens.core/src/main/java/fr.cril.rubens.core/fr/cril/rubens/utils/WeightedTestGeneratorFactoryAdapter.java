package fr.cril.rubens.utils;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A utility class used transform a {@link TestGeneratorFactory} type by adding weights to its translators.
 * 
 * Adding weights results in adding several instances of the concerned translator into the list of available translators.
 * Weights must be positive or null.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under consideration
 */
@ReflectorParam(enabled=false)
public abstract class WeightedTestGeneratorFactoryAdapter<T extends Instance> implements TestGeneratorFactory<T> {
	
	protected final Logger logger = LoggerHelper.getInstance().getLogger();
	
	@Override
	public List<InstanceTranslator<T>> translators() {
		final List<InstanceTranslator<T>> translators = initTranslators();
		final Map<InstanceTranslator<T>, Integer> weights = translatorWeights();
		if(!new HashSet<>(translators).equals(weights.keySet())) {
			final IllegalArgumentException exception = new IllegalArgumentException("exactly one weight per translator must be provided");
			this.logger.error(exception.getMessage(), exception);
			throw exception;
		}
		final List<InstanceTranslator<T>> effectiveTranslators = new ArrayList<>();
		for(int i=0; i<weights.size(); ++i) {
			final InstanceTranslator<T> translator = translators.get(i);
			final int weight = weights.get(translator);
			if(weight < 0) {
				final IllegalArgumentException exception = new IllegalArgumentException("weights must be positive or null");
				this.logger.error(exception.getMessage(), exception);
				throw exception;
			}
			for(int j=0; j<weight; ++j) {
				effectiveTranslators.add(translator);
			}
		}
		return effectiveTranslators;
	}
	
	/**
	 * Returns the translators of the adapted factory.
	 * 
	 * @return the translators of the adapted factory
	 */
	protected abstract List<InstanceTranslator<T>> initTranslators();
	
	/**
	 * Returns the mappings from the translators to the weight they are associated to.
	 * 
	 * The mapping must be valid.
	 * The mapping is invalid if the keyset of the map is not equal to the set of translators returned by
	 * {@link WeightedTestGeneratorFactoryAdapter#initInstance()}, or if at least one weight is not strictly positive.
	 *  
	 * @return the mappings from the translators to the weight they are associated to
	 */
	protected abstract Map<InstanceTranslator<T>, Integer> translatorWeights();

}
