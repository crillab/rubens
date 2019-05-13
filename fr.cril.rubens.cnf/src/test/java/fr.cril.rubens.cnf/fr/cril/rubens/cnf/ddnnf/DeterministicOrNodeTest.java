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

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class DeterministicOrNodeTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic1() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(2, 1), new LiteralNode(3, 1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic2() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, new LiteralNode(Utils.nextIndex(), 1), Utils.buildXor(1, 2));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic3() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, Utils.buildXor(1, 2), new LiteralNode(Utils.nextIndex(), 1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic4() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(2, 1), new LiteralNode(3, 2));
	}
	
	@Test
	public void testModels() {
		assertEquals(Stream.of(Utils.buildModel(-1, 2), Utils.buildModel(1, -2)).collect(Collectors.toSet()), new HashSet<>(Utils.buildXor(1, 2).models()));
	}
	
	@Test
	public void testOrNodeHasFalseAsChild() throws DDNNFException {
		final INode falseNode = new FalseNode(0);
		final INode trueNode = new TrueNode(1);
		final INode litNode2 = new LiteralNode(2, -1);
		final INode andNode3 = new DecomposableAndNode(3, Stream.of(litNode2, trueNode).collect(Collectors.toList()));
		final INode andNode4 = new DecomposableAndNode(4, Stream.of(falseNode).collect(Collectors.toList()));
		new DeterministicOrNode(5, 1, andNode4, andNode3);
	}
	
}
