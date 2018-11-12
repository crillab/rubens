package fr.cril.rubens.cnf.core;

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
@ReflectorParam(name="CNF")
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
