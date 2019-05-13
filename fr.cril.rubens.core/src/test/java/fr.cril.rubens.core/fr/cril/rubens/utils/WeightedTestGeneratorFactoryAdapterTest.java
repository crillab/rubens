package fr.cril.rubens.utils;

/*-
 * #%L
 * RUBENS core API
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

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.core.TestGenerator;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.testutils.StringConcatGeneratorFactory;
import fr.cril.rubens.testutils.StringInstance;

public class WeightedTestGeneratorFactoryAdapterTest {
	
	private StringConcatGeneratorFactory factory;

	@Before
	public void setUp() {
		this.factory = new StringConcatGeneratorFactory();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeWeights() {
		new WeightedStringConcatGeneratorFactory(this.factory, new int[] {-1, -1}).translators();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotEnoughMuchWeights() {
		new WeightedStringConcatGeneratorFactory(this.factory, new int[] {1}).translators();
	}
	
	@Test
	public void testJustOnes() {
		final WeightedStringConcatGeneratorFactory adaptedFactory = new WeightedStringConcatGeneratorFactory(this.factory, new int[] {1, 0});
		final List<InstanceTranslator<StringInstance>> effectiveTranslators = adaptedFactory.translators();
		assertEquals(1, effectiveTranslators.size());
		assertEquals(this.factory.translators().get(0), effectiveTranslators.get(0));
		final TestGenerator<StringInstance> generator = new TestGenerator<>(adaptedFactory);
		final List<StringInstance> instances = generator.computeToDepth(3);
		Collections.sort(instances, (i1,i2) -> i1.str().compareTo(i2.str()));
		assertEquals(3, instances.size());
		for(int i=0; i<3; ++i) {
			assertEquals(IntStream.range(0, i).mapToObj(j -> "1").reduce("", (a,b) -> a+b), instances.get(i).str());
		}
	}
	
	@ReflectorParam(enabled=false)
	private class WeightedStringConcatGeneratorFactory extends WeightedTestGeneratorFactoryAdapter<StringInstance> {
		
		private final StringConcatGeneratorFactory factory;
		
		private final int[] weights;

		private WeightedStringConcatGeneratorFactory(final StringConcatGeneratorFactory factory, final int[] weights) {
			this.factory = factory;
			this.weights = weights;
		}

		@Override
		public StringInstance initInstance() {
			return factory.initInstance();
		}

		@Override
		protected List<InstanceTranslator<StringInstance>> initTranslators() {
			return factory.translators();
		}

		@Override
		protected Map<InstanceTranslator<StringInstance>, Integer> translatorWeights() {
			final List<InstanceTranslator<StringInstance>> translators = factory.translators();
			final Map<InstanceTranslator<StringInstance>, Integer> weightsMap = new HashMap<>();
			IntStream.range(0, Math.min(translators.size(), weights.length)).forEach(i -> weightsMap.put(translators.get(i), this.weights[i]));
			return weightsMap;
		}

	}

}
