package fr.cril.rubens.cnf.ddnnf;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.cnf.core.CnfTestGeneratorFactory;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;

class DDNNFCheckerFactoryTest {
	
	private DDNNFCheckerFactory factory;
	
	@BeforeEach
	public void setUp() {
		this.factory = new DDNNFCheckerFactory();
	}
	
	@Test
	void testNewGenerator() {
		assertTrue(this.factory.newTestGenerator() instanceof CnfTestGeneratorFactory);
	}
	
	@Test
	void testNewExecutor() {
		final Path path = Paths.get("foo", "bar");
		assertTrue(this.factory.newExecutor(path) instanceof CnfSolverExecutor);
	}
	
	@Test
	void testOk() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 1 0 1\nL 1"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"nnf 0 0 1\nL 1", "nnf 1 0 1\nA 0", "nnf 1 0 1\nO 0 0"})
	void testSoftwareOutputFailure(final String arg) {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertFalse(factory.checkSoftwareOutput(instance, arg).isSuccessful());
	}
	
	@Test
	void testNoConstraints() {
		final CnfInstance instance = new CnfInstance(2, Collections.emptyList(), Stream.of(
				Stream.of(-1, -2).collect(Collectors.toList()),
				Stream.of(-1, 2).collect(Collectors.toList()),
				Stream.of(1, -2).collect(Collectors.toList()),
				Stream.of(1, 2).collect(Collectors.toList())
		).collect(Collectors.toList()));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 2 0 2\nO 0 0\nA 0\n"));
	}
	
	@Test
	void testOptions() {
		final List<MethodOption> opts = this.factory.getOptions();
		assertEquals(2, opts.size());
		assertTrue(opts.stream().anyMatch(o -> o.getName().equals("ignoreUnsat")));
		assertTrue(opts.stream().anyMatch(o -> o.getName().equals("ignorePreamble")));
	}
	
	@Test
	void testDoNotIgnPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		opt.apply("off");
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertFalse(factory.checkSoftwareOutput(instance, "nnf 0 0 1\nL 1").isSuccessful());
	}
	
	@Test
	void testIgnPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		opt.apply("on");
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.singletonList(Collections.singletonList(1)));
		assertEquals(CheckResult.SUCCESS, factory.checkSoftwareOutput(instance, "nnf 0 0 1\nL 1"));
	}
	
	@Test
	void testIllegalArgPreamble() {
		final MethodOption opt = this.factory.getOptions().stream().filter(o -> o.getName().equals("ignorePreamble")).findFirst().orElseThrow();
		assertThrows(IllegalArgumentException.class, () -> opt.apply("foo"));
	}
	
}
