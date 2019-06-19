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

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class DeterministicOrNodeTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullChild1() throws DDNNFException {
		new DeterministicOrNode(0, 1, null, new LiteralNode(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullChild2() throws DDNNFException {
		new DeterministicOrNode(0, 1, new LiteralNode(1), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegCflVar() throws DDNNFException {
		new DeterministicOrNode(0, -1, new LiteralNode(1), new LiteralNode(-1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic1() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(1), new LiteralNode(1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic2() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, new LiteralNode(1), Utils.buildXor(1, 2));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic3() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, Utils.buildXor(1, 2), new LiteralNode(1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic4() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(1), new LiteralNode(2));
	}
	
	@Test
	public void testModels() {
		assertEquals(Stream.of(Utils.buildModel(-1, 2), Utils.buildModel(1, -2)).collect(Collectors.toSet()), new HashSet<>(Utils.buildXor(1, 2).models()));
	}
	
	@Test
	public void testOrNodeHasFalseAsChild() throws DDNNFException {
		final INode falseNode = FalseNode.getInstance();
		final INode trueNode = TrueNode.getInstance();
		final INode litNode2 = new LiteralNode(-1);
		final INode andNode3 = new DecomposableAndNode(3, Stream.of(litNode2, trueNode).collect(Collectors.toList()));
		final INode andNode4 = new DecomposableAndNode(4, Stream.of(falseNode).collect(Collectors.toList()));
		final DeterministicOrNode orNode = new DeterministicOrNode(5, 1, andNode4, andNode3);
		assertEquals(1, orNode.models().size());
	}
	
	@Test
	public void testDisjWithFalse1() throws DDNNFException {
		final DeterministicOrNode orNode = new DeterministicOrNode(0, 1, FalseNode.getInstance(), new LiteralNode(1));
		assertEquals(Collections.singletonList(Collections.singletonMap(1, true)), orNode.models());
	}
	
	@Test
	public void testDisjWithFalse2() throws DDNNFException {
		final DeterministicOrNode orNode = new DeterministicOrNode(0, 1, new LiteralNode(1), FalseNode.getInstance());
		assertEquals(Collections.singletonList(Collections.singletonMap(1, true)), orNode.models());
	}
	
	@Test
	public void testEquals() throws DDNNFException {
		EqualsVerifier.forClass(DeterministicOrNode.class)
			.withNonnullFields("child1", "child2")
			.withIgnoredFields("models")
			.withCachedHashCode("hash", "computeHash", new DeterministicOrNode(0, 1, new LiteralNode(1), new LiteralNode(-1)))
			.verify();
	}
	
	@Test
	public void testRevEquals() throws DDNNFException {
		final LiteralNode posNode = new LiteralNode(1);
		final LiteralNode negNode = new LiteralNode(-1);
		assertEquals(new DeterministicOrNode(0, 1, posNode, negNode), new DeterministicOrNode(0, 1, negNode, posNode));
	}
	
}
