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
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractNodeTest {
	
	private Node n1;
	
	private Node n2;
	
	private Node n3;
	
	private Node n4;
	
	@Before
	public void setUp() {
		this.n1 = new Node(1, Collections.emptyList());
		this.n2 = new Node(2, Collections.emptyList());
		final List<Map<Integer, Boolean>> models = Collections.singletonList(Collections.singletonMap(1, true));
		this.n3 = new Node(1, Collections.emptyList());
		this.n4 = new Node(2, models);
	}
	
	@Test
	public void testModels() {
		assertEquals(Collections.emptyList(), this.n1.models());
		assertEquals(Collections.singletonList(Collections.singletonMap(1, true)), this.n4.models());
	}
	
	@Test
	public void testHashcode() {
		assertEquals(1, this.n1.hashCode());
		assertEquals(2, this.n2.hashCode());
		assertEquals(1, this.n1.hashCode());
	}
	
	@Test
	public void testEquals() {
		assertEquals(this.n1, this.n3);
		assertNotEquals(this.n1, this.n2);
		assertNotEquals(this.n1, null);
		assertNotEquals(this.n1, new Object());
	}
	
	private class Node extends AbstractNode {

		protected Node(final int nodeIndex, final List<Map<Integer, Boolean>> models) {
			super(nodeIndex, models);
		}
		
	}

}
