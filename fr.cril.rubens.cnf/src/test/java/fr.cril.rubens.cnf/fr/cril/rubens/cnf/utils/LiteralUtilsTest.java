package fr.cril.rubens.cnf.utils;

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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class LiteralUtilsTest {

	@Test
	public void testDimacsToInternal() {
		assertEquals(0, LiteralUtils.dimacsToInternal(1));
		assertEquals(1, LiteralUtils.dimacsToInternal(-1));
		assertEquals(2, LiteralUtils.dimacsToInternal(2));
		assertEquals(3, LiteralUtils.dimacsToInternal(-2));
	}

	@Test
	public void testSelectRandomLiteral() {
		for(int i=0; i<10; ++i) {
			assertEquals(1, LiteralUtils.selectRandomLiteral(Stream.of(0, 1, 0).collect(Collectors.toList())));
		}

	}
}
