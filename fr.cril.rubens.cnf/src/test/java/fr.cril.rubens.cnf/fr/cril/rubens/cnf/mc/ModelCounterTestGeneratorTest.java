package fr.cril.rubens.cnf.mc;

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

import org.junit.Test;

import fr.cril.rubens.core.TestGenerator;

public class ModelCounterTestGeneratorTest {
	
	@Test
	public void testMoreThanOneTest0() {
		final TestGenerator<ModelCountingCnfInstance> generator = new TestGenerator<>(new ModelCountingCnfTestGeneratorFactory());
		final List<ModelCountingCnfInstance> instances = generator.computeToDepth(2);
		assertEquals(2, instances.size());
	}
	
	@Test
	public void testMoreThanOneTest1() {
		final TestGenerator<ModelCountingCnfInstance> generator = new TestGenerator<>(new ModelCountingCnfTestGeneratorFactory());
		this.counter = 0;
		generator.computeToDepth(2, i -> incCounter());
		assertEquals(2, this.counter);
	}
	
	private int counter;
	
	private void incCounter() {
		this.counter++;
	}

}
