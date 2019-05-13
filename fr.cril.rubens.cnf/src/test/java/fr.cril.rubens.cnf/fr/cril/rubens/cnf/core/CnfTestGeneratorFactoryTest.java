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

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.specs.InstanceTranslator;

public class CnfTestGeneratorFactoryTest {
	
	private CnfTestGeneratorFactory generator;

	@Before
	public void setUp() {
		this.generator = new CnfTestGeneratorFactory();
	}
	
	@Test
	public void testInitInstance() {
		assertEquals(new CnfInstance(), this.generator.initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<CnfInstance>> initTranslators = this.generator.initTranslators();
		final Set<InstanceTranslator<CnfInstance>> initTranslatorsSet = initTranslators.stream().distinct().collect(Collectors.toSet());
		final List<InstanceTranslator<CnfInstance>> translators = this.generator.translators();
		assertEquals(initTranslatorsSet, translators.stream().distinct().collect(Collectors.toSet()));
	}
	
	@Test
	public void testTranslatorsWeights() {
		final Map<InstanceTranslator<CnfInstance>, Integer> weights = this.generator.translatorWeights();
		final List<InstanceTranslator<CnfInstance>> translators = this.generator.translators();
		for(final Entry<InstanceTranslator<CnfInstance>, Integer> weightEntry : weights.entrySet()) {
			assertEquals((long) weightEntry.getValue(), translators.stream().filter(weightEntry.getKey()::equals).count());
		}
	}

}
