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

import org.junit.jupiter.api.Test;

class DDNNFExceptionTest {
	
	@Test
	void testNewSyntaxErrorInPreamble() {
		assertEquals("syntax error in preamble", DDNNFException.newSyntaxErrorInPreamble().getMessage());
	}
	
	@Test
	void testNewErrorAtNode() {
		assertEquals("an error was detected at node which index is 1 (foo)", DDNNFException.newErrorAtNode(1, "foo").getMessage());
	}
	
	@Test
	void testNewWrongNumberOfNodesInPreamble() {
		assertEquals("wrong number of declared nodes (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfNodesInPreamble(1, 2).getMessage());
	}
	
	@Test
	void testNewWrongNumberOfEdgesInPreamble() {
		assertEquals("wrong number of declared edges (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfEdgesInPreamble(1, 2).getMessage());
	}
	
	@Test
	void testNewNotDecomposableAndNode() {
		assertEquals("AND node at index 1 is not decomposable", DDNNFException.newNotDecomposableAndNode(1).getMessage());
	}
	
	@Test
	void testNewNotDeterministOrNode() {
		assertEquals("OR node at index 1 is not determinist", DDNNFException.newNotDeterministOrNode(1).getMessage());
	}

}
