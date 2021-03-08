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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class DDNNFTest {
	
	@BeforeEach
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test
	void testModels() {
		final DDNNF ddnnf = new DDNNF(2, Utils.buildXor(1, 2));
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	void testModelsWithFreeVars() {
		final DDNNF ddnnf = new DDNNF(4, Utils.buildXor(1, 2));
		assertEquals(8, Utils.countModels(ddnnf));
	}
	
	@Test
	void testModelsWithFreeVars2() throws DDNNFException {
		final INode trueNode = TrueNode.getInstance();
		final INode litNode = new LiteralNode(-1);
		final INode andNode = new DecomposableAndNode(3, Stream.of(litNode, trueNode).collect(Collectors.toList()));
		final DDNNF ddnnf = new DDNNF(3, andNode);
		final List<List<Integer>> actual = new ArrayList<>(Utils.models(ddnnf));
		final List<List<Integer>> expected = Stream.of(
			Stream.of(-1, -2, -3).collect(Collectors.toList()),
			Stream.of(-1, -2, 3).collect(Collectors.toList()),
			Stream.of(-1, 2, -3).collect(Collectors.toList()),
			Stream.of(-1, 2, 3).collect(Collectors.toList())
		).collect(Collectors.toList());
		assertEquals(expected, actual);
	}
	
	@Test
	void testNoConstraints() {
		final DDNNF ddnnf = new DDNNF(1);
		final List<List<Integer>> models = Utils.models(ddnnf);
		assertEquals(2, models.size());
		assertTrue(models.contains(Collections.singletonList(1)));
		assertTrue(models.contains(Collections.singletonList(-1)));
	}
	
	@Test
	void testNoVars() {
		final DDNNF ddnnf = new DDNNF(0, TrueNode.getInstance());
		assertEquals(Collections.singletonList(Collections.emptyList()), Utils.models(ddnnf));
	}
	
	@Test
	void testEquals() {
		EqualsVerifier.forClass(DDNNF.class).verify();
	}
}
