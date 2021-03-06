package fr.cril.rubens.cnf.mc;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.CnfTestGeneratorFactory;
import fr.cril.rubens.cnf.utils.CnfTranslatorAdapter;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.WeightedTestGeneratorFactoryAdapter;

/**
 * A {@link TestGeneratorFactory} instance dedicated to model counting.
 * 
 * It is named <code>MCCNF</code> for the {@link TranslatorGeneratorReflector}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="MCCNF", family="CNF")
public class ModelCountingCnfTestGeneratorFactory extends WeightedTestGeneratorFactoryAdapter<ModelCountingCnfInstance> {

	private final List<InstanceTranslator<ModelCountingCnfInstance>> initTranslators;

	private final Map<InstanceTranslator<ModelCountingCnfInstance>, Integer> translatorWeights;

	/**
	 * Builds a new instance of the factory.
	 */
	public ModelCountingCnfTestGeneratorFactory() {
		final CnfTestGeneratorFactory cnfFactory = new CnfTestGeneratorFactory();
		this.initTranslators = new ArrayList<>();
		this.translatorWeights = new HashMap<>();
		for(final Entry<InstanceTranslator<CnfInstance>, Integer> entry : cnfFactory.translatorWeights().entrySet()) {
			final CnfTranslatorAdapter<CnfInstance, ModelCountingCnfInstance> adaptedTranslator = new CnfTranslatorAdapter<>(entry.getKey());
			this.initTranslators.add(adaptedTranslator);
			translatorWeights.put(adaptedTranslator, entry.getValue());
		}
	}

	@Override
	public ModelCountingCnfInstance initInstance() {
		return new ModelCountingCnfInstance();
	}

	@Override
	protected List<InstanceTranslator<ModelCountingCnfInstance>> initTranslators() {
		return this.initTranslators;
	}

	@Override
	protected Map<InstanceTranslator<ModelCountingCnfInstance>, Integer> translatorWeights() {
		return this.translatorWeights;
	}

}
