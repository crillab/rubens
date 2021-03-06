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
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewClauseTranslatorTest {
	
	private NewClauseTranslator translator;

	@BeforeEach
	public void setUp() {
		this.translator = new NewClauseTranslator();
	}
	
	@Test
	void testCanBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	void testCannotBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList()))
				.collect(Collectors.toList()), Collections.emptyList());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()), newInstance.clauses());
		assertEquals(Collections.emptyList(), newInstance.models());
	}
	
	@Test
	void testApplyFreeVar() {
		final CnfInstance instance = new CnfInstance(1, new ArrayList<>(),
				Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		final boolean posLitClause = Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean posLitModels = Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.models());
		final boolean negLitClause = Stream.of(Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean negLitModels = Stream.of(Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.models());
		assertTrue(posLitClause || negLitClause);
		assertTrue((!posLitClause) || posLitModels);
		assertTrue((!negLitClause) || negLitModels);
	}
	
	@Test
	void testEmptyInstance() {
		assertFalse(this.translator.canBeAppliedTo(new CnfInstance()));
	}

}
