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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class NewClauseTranslatorTest {
	
	private NewClauseTranslator translator;

	@Before
	public void setUp() {
		this.translator = new NewClauseTranslator();
	}
	
	@Test
	public void testCanBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		assertTrue(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testCannotBeAppliedTo() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList()))
				.collect(Collectors.toList()), Collections.emptySet());
		assertFalse(this.translator.canBeAppliedTo(instance));
	}
	
	@Test
	public void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		assertEquals(Stream.of(Stream.of(1).collect(Collectors.toList()), Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toSet()), new HashSet<>(newInstance.clauses()));
		assertEquals(Collections.emptySet(), newInstance.models());
	}
	
	@Test
	public void testApplyFreeVar() {
		final CnfInstance instance = new CnfInstance(1, new ArrayList<>(),
				Stream.of(Stream.of(1).collect(Collectors.toSet()), Stream.of(-1).collect(Collectors.toSet())).collect(Collectors.toSet()));
		final CnfInstance newInstance = this.translator.translate(instance);
		assertEquals(instance.nVars(), newInstance.nVars());
		final boolean posLitClause = Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean posLitModels = Stream.of(Stream.of(1).collect(Collectors.toSet())).collect(Collectors.toSet()).equals(newInstance.models());
		final boolean negLitClause = Stream.of(Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()).equals(newInstance.clauses());
		final boolean negLitModels = Stream.of(Stream.of(-1).collect(Collectors.toSet())).collect(Collectors.toSet()).equals(newInstance.models());
		assertTrue(posLitClause || negLitClause);
		assertTrue((!posLitClause) || posLitModels);
		assertTrue((!negLitClause) || negLitModels);
	}
	
	@Test
	public void testEmptyInstance() {
		assertFalse(this.translator.canBeAppliedTo(new CnfInstance()));
	}

}
