package fr.cril.rubens.cnf.wmc;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.specs.InstanceTranslator;

public class WeightedModelCountingCnfTestGeneratorFactoryTest {
	
	private WeightedModelCountingCnfTestGeneratorFactory generator;

	@Before
	public void setUp() {
		this.generator = new WeightedModelCountingCnfTestGeneratorFactory();
	}

	@Test
	public void testInitInstance() {
		assertEquals(new WeightedModelCountingCnfInstance(), this.generator.initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<WeightedModelCountingCnfInstance>> initTranslators = this.generator.initTranslators();
		final Set<InstanceTranslator<WeightedModelCountingCnfInstance>> initTranslatorsSet = initTranslators.stream().distinct().collect(Collectors.toSet());
		final List<InstanceTranslator<WeightedModelCountingCnfInstance>> translators = this.generator.translators();
		assertEquals(initTranslatorsSet, translators.stream().distinct().collect(Collectors.toSet()));
	}
	
	@Test
	public void testTranslatorsWeights() {
		final Map<InstanceTranslator<WeightedModelCountingCnfInstance>, Integer> weights = this.generator.translatorWeights();
		final List<InstanceTranslator<WeightedModelCountingCnfInstance>> translators = this.generator.translators();
		for(final Entry<InstanceTranslator<WeightedModelCountingCnfInstance>, Integer> weightEntry : weights.entrySet()) {
			assertEquals((long) weightEntry.getValue(), translators.stream().filter(weightEntry.getKey()::equals).count());
		}
	}
}
