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

public class DecomposableAndNodeTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoChildren() throws DDNNFException {
		new DecomposableAndNode(1, Collections.emptyList());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullChildren() throws DDNNFException {
		new DecomposableAndNode(1, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullChild() throws DDNNFException {
		new DecomposableAndNode(1, Collections.singletonList(null));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDecomposable() throws DDNNFException {
		new DecomposableAndNode(1, Stream.of(new LiteralNode(1), new LiteralNode(-1)).collect(Collectors.toList()));
	}
	
	@Test
	public void testModels() throws DDNNFException {
		final DecomposableAndNode node = new DecomposableAndNode(Utils.nextIndex(), Stream.of(Utils.buildXor(1, 2), Utils.buildXor(3, 4)).collect(Collectors.toList()));
		assertEquals(Stream.of(
				Utils.buildModel(1, -2, 3, -4),
				Utils.buildModel(1, -2, -3, 4),
				Utils.buildModel(-1, 2, 3, -4),
				Utils.buildModel(-1, 2, -3, 4)
		).collect(Collectors.toSet()), new HashSet<>(node.models()));
	}
	
	@Test
	public void testEquals() throws DDNNFException {
		EqualsVerifier.forClass(DecomposableAndNode.class)
			.withNonnullFields("children")
			.withIgnoredFields("models")
			.withCachedHashCode("hash", "computeHash", new DecomposableAndNode(0, Stream.of(TrueNode.getInstance(), FalseNode.getInstance()).collect(Collectors.toList())))
			.verify();
	}
	
}
