package fr.cril.rubens.cnf.ddnnf;

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

import org.junit.Test;

public class DDNNFExceptionTest {
	
	@Test
	public void testNewSyntaxErrorInPreamble() {
		assertEquals("syntax error in preamble", DDNNFException.newSyntaxErrorInPreamble().getMessage());
	}
	
	@Test
	public void testNewErrorAtNode() {
		assertEquals("an error was detected at node which index is 1 (foo)", DDNNFException.newErrorAtNode(1, "foo").getMessage());
	}
	
	@Test
	public void testNewWrongNumberOfNodesInPreamble() {
		assertEquals("wrong number of declared nodes (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfNodesInPreamble(1, 2).getMessage());
	}
	
	@Test
	public void testNewWrongNumberOfEdgesInPreamble() {
		assertEquals("wrong number of declared edges (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfEdgesInPreamble(1, 2).getMessage());
	}
	
	@Test
	public void testNewNotDecomposableAndNode() {
		assertEquals("AND node at index 1 is not decomposable", DDNNFException.newNotDecomposableAndNode(1).getMessage());
	}
	
	@Test
	public void testNewNotDeterministOrNode() {
		assertEquals("OR node at index 1 is not determinist", DDNNFException.newNotDeterministOrNode(1).getMessage());
	}

}
