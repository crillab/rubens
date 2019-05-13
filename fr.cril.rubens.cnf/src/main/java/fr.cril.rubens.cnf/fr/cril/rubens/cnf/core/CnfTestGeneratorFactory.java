package fr.cril.rubens.cnf.core;

/*-
 * #%L
 * RUBENS module for CNF handling
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.WeightedTestGeneratorFactoryAdapter;

/**
 * A {@link TestGeneratorFactory} instance dedicated to CNF instances.
 * 
 * It is named <code>CNF</code> for the {@link TranslatorGeneratorReflector}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="CNF", family="CNF")
public class CnfTestGeneratorFactory extends WeightedTestGeneratorFactoryAdapter<CnfInstance> {
	
	private final List<InstanceTranslator<CnfInstance>> translators = new ArrayList<>();
	
	private final Map<InstanceTranslator<CnfInstance>, Integer> weights = new HashMap<>();
	
	/**
	 * Builds a new instance of the factory.
	 */
	public CnfTestGeneratorFactory() {
		for(final Translator translator : Translator.values()) {
			this.translators.add(translator.getTranslator());
			this.weights.put(translator.getTranslator(), translator.getWeight());
		}
	}

	@Override
	public CnfInstance initInstance() {
		return new CnfInstance();
	}

	@Override
	public List<InstanceTranslator<CnfInstance>> initTranslators() {
		return Collections.unmodifiableList(this.translators);
	}

	@Override
	public Map<InstanceTranslator<CnfInstance>, Integer> translatorWeights() {
		return Collections.unmodifiableMap(this.weights);
	}
	
	private enum Translator {

		NEW_VAR(new NewVarTranslator(), 1),

		NEW_CLAUSE(new NewClauseTranslator(), 1),

		NEW_LIT(new NewLitInClauseTranslator(), 2);

		private final InstanceTranslator<CnfInstance> localTranslator;

		private final int weight;

		private Translator(final InstanceTranslator<CnfInstance> translator, final int weight) {
			this.localTranslator = translator;
			this.weight = weight;
		}

		private InstanceTranslator<CnfInstance> getTranslator() {
			return this.localTranslator;
		}

		private int getWeight() {
			return this.weight;
		}
	}

}
