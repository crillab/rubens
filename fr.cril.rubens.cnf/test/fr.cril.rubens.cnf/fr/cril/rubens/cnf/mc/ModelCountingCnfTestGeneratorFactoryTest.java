package fr.cril.rubens.cnf.mc;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.specs.InstanceTranslator;

public class ModelCountingCnfTestGeneratorFactoryTest {
	
	private ModelCountingCnfTestGeneratorFactory generator;

	@Before
	public void setUp() {
		this.generator = new ModelCountingCnfTestGeneratorFactory();
	}

	@Test
	public void testInitInstance() {
		assertEquals(new ModelCountingCnfInstance(), this.generator.initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<ModelCountingCnfInstance>> initTranslators = this.generator.initTranslators();
		final Set<InstanceTranslator<ModelCountingCnfInstance>> initTranslatorsSet = initTranslators.stream().distinct().collect(Collectors.toSet());
		final List<InstanceTranslator<ModelCountingCnfInstance>> translators = this.generator.translators();
		assertEquals(initTranslatorsSet, translators.stream().distinct().collect(Collectors.toSet()));
	}
	
	@Test
	public void testTranslatorsWeights() {
		final Map<InstanceTranslator<ModelCountingCnfInstance>, Integer> weights = this.generator.translatorWeights();
		final List<InstanceTranslator<ModelCountingCnfInstance>> translators = this.generator.translators();
		for(final Entry<InstanceTranslator<ModelCountingCnfInstance>, Integer> weightEntry : weights.entrySet()) {
			assertEquals((long) weightEntry.getValue(), translators.stream().filter(weightEntry.getKey()::equals).count());
		}
	}
}
