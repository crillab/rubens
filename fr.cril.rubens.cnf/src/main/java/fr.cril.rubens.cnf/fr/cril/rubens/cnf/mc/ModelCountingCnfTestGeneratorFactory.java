package fr.cril.rubens.cnf.mc;

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
@ReflectorParam(name="MCCNF")
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
