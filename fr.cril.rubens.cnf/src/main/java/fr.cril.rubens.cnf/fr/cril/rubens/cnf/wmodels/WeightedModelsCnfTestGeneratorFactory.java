package fr.cril.rubens.cnf.wmodels;

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
 * A {@link TestGeneratorFactory} instance dedicated to CNF formulas with weighted models.
 * 
 * It is named <code>WMCNF</code> for the {@link TranslatorGeneratorReflector}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="WMCNF", family="CNF")
public class WeightedModelsCnfTestGeneratorFactory extends WeightedTestGeneratorFactoryAdapter<WeightedModelsCnfInstance> {
	
	private final List<InstanceTranslator<WeightedModelsCnfInstance>> initTranslators;
	
	private final Map<InstanceTranslator<WeightedModelsCnfInstance>, Integer> translatorWeights;

	/**
	 * Builds a new instance of the factory.
	 */
	public WeightedModelsCnfTestGeneratorFactory() {
		final CnfTestGeneratorFactory cnfFactory = new CnfTestGeneratorFactory();
		this.initTranslators = new ArrayList<>();
		this.translatorWeights = new HashMap<>();
		for(final Entry<InstanceTranslator<CnfInstance>, Integer> entry : cnfFactory.translatorWeights().entrySet()) {
			final CnfTranslatorAdapter<CnfInstance, WeightedModelsCnfInstance> adaptedTranslator = new CnfTranslatorAdapter<>(entry.getKey());
			this.initTranslators.add(adaptedTranslator);
			translatorWeights.put(adaptedTranslator, entry.getValue());
		}
	}

	@Override
	public WeightedModelsCnfInstance initInstance() {
		return new WeightedModelsCnfInstance();
	}

	@Override
	protected List<InstanceTranslator<WeightedModelsCnfInstance>> initTranslators() {
		return this.initTranslators;
	}

	@Override
	public Map<InstanceTranslator<WeightedModelsCnfInstance>, Integer> translatorWeights() {
		return this.translatorWeights;
	}
	
}
