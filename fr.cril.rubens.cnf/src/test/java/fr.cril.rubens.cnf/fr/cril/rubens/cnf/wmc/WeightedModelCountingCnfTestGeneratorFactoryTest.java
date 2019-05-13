package fr.cril.rubens.cnf.wmc;

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
