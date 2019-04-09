package fr.cril.rubens.cnf.mc;

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
