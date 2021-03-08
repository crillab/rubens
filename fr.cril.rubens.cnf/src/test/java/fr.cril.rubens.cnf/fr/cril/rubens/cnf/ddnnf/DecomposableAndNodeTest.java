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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class DecomposableAndNodeTest {
	
	@BeforeEach
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test
	void testNoChildren() throws DDNNFException {
		List<INode> empty = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> new DecomposableAndNode(1, empty));
	}
	
	@Test
	void testNullChildren() throws DDNNFException {
		assertThrows(IllegalArgumentException.class, () -> new DecomposableAndNode(1, null));
	}
	
	@Test
	void testNullChild() throws DDNNFException {
		List<INode> singleNull = Collections.singletonList(null);
		assertThrows(IllegalArgumentException.class, () -> new DecomposableAndNode(1, singleNull));
	}
	
	@Test
	void testNotDecomposable() throws DDNNFException {
		List<INode> children = Stream.of(new LiteralNode(1), new LiteralNode(-1)).collect(Collectors.toList());
		assertThrows(DDNNFException.class, () -> new DecomposableAndNode(1, children));
	}
	
	@Test
	void testModels() throws DDNNFException {
		final DecomposableAndNode node = new DecomposableAndNode(Utils.nextIndex(), Stream.of(Utils.buildXor(1, 2), Utils.buildXor(3, 4)).collect(Collectors.toList()));
		assertEquals(Stream.of(
				Utils.buildModel(1, -2, 3, -4),
				Utils.buildModel(1, -2, -3, 4),
				Utils.buildModel(-1, 2, 3, -4),
				Utils.buildModel(-1, 2, -3, 4)
		).collect(Collectors.toSet()), new HashSet<>(node.models()));
	}
	
	@Test
	void testEquals() throws DDNNFException {
		EqualsVerifier.forClass(DecomposableAndNode.class)
			.withNonnullFields("children")
			.withIgnoredFields("models")
			.withCachedHashCode("hash", "computeHash", new DecomposableAndNode(0, Stream.of(TrueNode.getInstance(), FalseNode.getInstance()).collect(Collectors.toList())))
			.verify();
	}
	
}
