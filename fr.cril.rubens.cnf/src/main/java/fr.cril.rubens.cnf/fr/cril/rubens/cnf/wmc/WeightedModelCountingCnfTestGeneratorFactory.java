package fr.cril.rubens.cnf.wmc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.cril.rubens.cnf.utils.CnfTranslatorAdapter;
import fr.cril.rubens.cnf.wmodels.WeightedModelsCnfInstance;
import fr.cril.rubens.cnf.wmodels.WeightedModelsCnfTestGeneratorFactory;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.reflection.TranslatorGeneratorReflector;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.WeightedTestGeneratorFactoryAdapter;

/**
 * A {@link TestGeneratorFactory} instance dedicated to weighted model counting.
 * 
 * It is named <code>WMCCNF</code> for the {@link TranslatorGeneratorReflector}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="WMCCNF", family="CNF")
public class WeightedModelCountingCnfTestGeneratorFactory extends WeightedTestGeneratorFactoryAdapter<WeightedModelCountingCnfInstance> {
	
	private final List<InstanceTranslator<WeightedModelCountingCnfInstance>> initTranslators;
	
	private final Map<InstanceTranslator<WeightedModelCountingCnfInstance>, Integer> translatorWeights;

	/**
	 * Builds a new instance of the factory.
	 */
	public WeightedModelCountingCnfTestGeneratorFactory() {
		final WeightedModelsCnfTestGeneratorFactory wcnfFactory = new WeightedModelsCnfTestGeneratorFactory();
		this.initTranslators = new ArrayList<>();
		this.translatorWeights = new HashMap<>();
		for(final Entry<InstanceTranslator<WeightedModelsCnfInstance>, Integer> entry : wcnfFactory.translatorWeights().entrySet()) {
			final CnfTranslatorAdapter<WeightedModelsCnfInstance, WeightedModelCountingCnfInstance> adaptedTranslator = new CnfTranslatorAdapter<>(entry.getKey());
			this.initTranslators.add(adaptedTranslator);
			translatorWeights.put(adaptedTranslator, entry.getValue());
		}
	}

	@Override
	public WeightedModelCountingCnfInstance initInstance() {
		return new WeightedModelCountingCnfInstance();
	}

	@Override
	protected List<InstanceTranslator<WeightedModelCountingCnfInstance>> initTranslators() {
		return this.initTranslators;
	}

	@Override
	protected Map<InstanceTranslator<WeightedModelCountingCnfInstance>, Integer> translatorWeights() {
		return this.translatorWeights;
	}
	
}
