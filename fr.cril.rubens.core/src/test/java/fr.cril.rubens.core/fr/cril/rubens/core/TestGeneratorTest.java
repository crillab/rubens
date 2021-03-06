package fr.cril.rubens.core;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.testutils.LazyStringGeneratorFactory;
import fr.cril.rubens.testutils.StringConcatGeneratorFactory;
import fr.cril.rubens.testutils.StringInstance;

class TestGeneratorTest {
	
	private StringConcatGeneratorFactory factory;
	
	private TestGenerator<StringInstance> generator;

	@BeforeEach
	public void setUp() {
		this.factory = new StringConcatGeneratorFactory();
		this.generator = new TestGenerator<>(this.factory);
	}
	
	@Test
	void testNullDepth() {
		assertThrows(IllegalArgumentException.class, () -> this.generator.computeToDepth(0));
	}
	
	@Test
	void testRootDepth() {
		final List<StringInstance> instances = this.generator.computeToDepth(1);
		assertEquals(1, instances.size());
		assertEquals(this.factory.initInstance(), instances.get(0));
	}
	
	@Test
	void testDepth3() {
		final List<StringInstance> instances = this.generator.computeToDepth(3);
		assertEquals(7, instances.size());
		final Set<String> expectedStr = Stream.of("", "1", "2", "11", "12", "21", "22").collect(Collectors.toSet());
		assertEquals(expectedStr, instances.stream().map(StringInstance::str).collect(Collectors.toSet()));
	}
	
	@Test
	void testConsumerDepth3() {
		final Set<String> actualStr = new HashSet<>();
		this.generator.computeToDepth(3, i -> actualStr.add(i.str()));
		assertEquals(7, actualStr.size());
		final Set<String> expectedStr = Stream.of("", "1", "2", "11", "12", "21", "22").collect(Collectors.toSet());
		assertEquals(expectedStr, actualStr);
	}
	
	@Test
	void testNoGeneratorsCanBeApplied() {
		final TestGenerator<StringInstance> lazyGenerator = new TestGenerator<>(new LazyStringGeneratorFactory());
		final List<StringInstance> instances = lazyGenerator.computeToDepth(3);
		assertEquals(1, instances.size());
		assertEquals(this.factory.initInstance(), instances.get(0));
	}

}
