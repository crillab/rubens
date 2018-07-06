package fr.cril.rubens.core;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.testutils.LazyStringGeneratorFactory;
import fr.cril.rubens.testutils.StringConcatGeneratorFactory;
import fr.cril.rubens.testutils.StringInstance;

public class TestGeneratorTest {
	
	private StringConcatGeneratorFactory factory;
	
	private TestGenerator<StringInstance> generator;

	@Before
	public void setUp() {
		this.factory = new StringConcatGeneratorFactory();
		this.generator = new TestGenerator<>(this.factory);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullDepth() {
		this.generator.computeToDepth(0);
	}
	
	@Test
	public void testRootDepth() {
		final List<StringInstance> instances = this.generator.computeToDepth(1);
		assertEquals(1, instances.size());
		assertEquals(this.factory.initInstance(), instances.get(0));
	}
	
	@Test
	public void testDepth3() {
		final List<StringInstance> instances = this.generator.computeToDepth(3);
		assertEquals(7, instances.size());
		final Set<String> expectedStr = Stream.of("", "1", "2", "11", "12", "21", "22").collect(Collectors.toSet());
		assertEquals(expectedStr, instances.stream().map(StringInstance::str).collect(Collectors.toSet()));
	}
	
	@Test
	public void testConsumerDepth3() {
		final Set<String> actualStr = new HashSet<>();
		this.generator.computeToDepth(3, i -> actualStr.add(i.str()));
		assertEquals(7, actualStr.size());
		final Set<String> expectedStr = Stream.of("", "1", "2", "11", "12", "21", "22").collect(Collectors.toSet());
		assertEquals(expectedStr, actualStr);
	}
	
	@Test
	public void testNoGeneratorsCanBeApplied() {
		final TestGenerator<StringInstance> lazyGenerator = new TestGenerator<>(new LazyStringGeneratorFactory());
		final List<StringInstance> instances = lazyGenerator.computeToDepth(3);
		assertEquals(1, instances.size());
		assertEquals(this.factory.initInstance(), instances.get(0));
	}

}
