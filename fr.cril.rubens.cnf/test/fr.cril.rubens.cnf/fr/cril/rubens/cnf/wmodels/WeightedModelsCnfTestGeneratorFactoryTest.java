package fr.cril.rubens.cnf.wmodels;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.specs.InstanceTranslator;

public class WeightedModelsCnfTestGeneratorFactoryTest {
	
	private WeightedModelsCnfTestGeneratorFactory generator;

	@Before
	public void setUp() {
		this.generator = new WeightedModelsCnfTestGeneratorFactory();
	}
	
	@Test
	public void testInitInstance() {
		assertEquals(new CnfInstance(), this.generator.initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<WeightedModelsCnfInstance>> initTranslators = this.generator.initTranslators();
		final Set<InstanceTranslator<WeightedModelsCnfInstance>> initTranslatorsSet = initTranslators.stream().distinct().collect(Collectors.toSet());
		final List<InstanceTranslator<WeightedModelsCnfInstance>> translators = this.generator.translators();
		assertEquals(initTranslatorsSet, translators.stream().distinct().collect(Collectors.toSet()));
	}
	
	@Test
	public void testTranslatorsWeights() {
		final Map<InstanceTranslator<WeightedModelsCnfInstance>, Integer> weights = this.generator.translatorWeights();
		final List<InstanceTranslator<WeightedModelsCnfInstance>> translators = this.generator.translators();
		for(final Entry<InstanceTranslator<WeightedModelsCnfInstance>, Integer> weightEntry : weights.entrySet()) {
			assertEquals((long) weightEntry.getValue(), translators.stream().filter(weightEntry.getKey()::equals).count());
		}
	}

}
