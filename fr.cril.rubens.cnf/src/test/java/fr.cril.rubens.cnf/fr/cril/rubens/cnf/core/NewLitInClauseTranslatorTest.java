package fr.cril.rubens.cnf.core;

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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewLitInClauseTranslatorTest {
	
	private NewLitInClauseTranslator translator;

	@BeforeEach
	public void setUp() {
		this.translator = new NewLitInClauseTranslator();
	}
	
	@Test
	void testCanBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	void testCannotBeAppliedNoClauses() {
		final CnfInstance instance = new CnfInstance(1, new ArrayList<>(), new ArrayList<>());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	void testCannotBeAppliedFullClauses() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1, -1).collect(Collectors.toList())).collect(Collectors.toList()),
				Collections.emptyList());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(Stream.of(Stream.of(1, -1).collect(Collectors.toList())).collect(Collectors.toList()), newInstance.clauses());
		assertEquals(Stream.of(Stream.of(-1).collect(Collectors.toList()), Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()), newInstance.models());
	}
	
	@Test
	void testApplyFreeVars() {
		final CnfInstance instance = new CnfInstance(2, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{1, -2}, new int[]{1, 2})
				.map(m -> Arrays.stream(m).boxed().collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(1, newInstance.clauses().size());
		assertEquals(2, newInstance.clauses().iterator().next().size());
		assertTrue(newInstance.models().size() >= 3);
	}
	
	@Test
	void testApplyFreeVars2() {
		for(int i=0; i<8; ++i) {
			final CnfInstance instance = new CnfInstance(3, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
					Stream.of(new int[]{1, -2, -3}, new int[]{1, -2, 3}, new int[]{1, 2, -3}, new int[]{1, 2, 3})
					.map(m -> Arrays.stream(m).boxed().collect(Collectors.toList())).collect(Collectors.toList()));
			final CnfInstance newInstance = this.translator.translate(instance);
			assertEquals(instance.nVars(), newInstance.nVars());
			assertEquals(1, newInstance.clauses().size());
			assertEquals(2, newInstance.clauses().iterator().next().size());
			assertTrue(newInstance.models().size() >= 6);
		}
	}
	
	@Test
	void testApplyFullClause() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(-1, 1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{-1}).map(m -> Arrays.stream(m).boxed().collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(2, newInstance.clauses().size());
		assertEquals(2, newInstance.clauses().iterator().next().size());
		assertEquals(2, newInstance.models().size());
	}
	
	@Test
	void applyFullClauses() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(-1, 1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(new int[]{-1}, new int[]{1}).map(m -> Arrays.stream(m).boxed().collect(Collectors.toList())).collect(Collectors.toList()));
		assertThrows(IllegalArgumentException.class, () -> this.translator.translate(instance));
	}

}
