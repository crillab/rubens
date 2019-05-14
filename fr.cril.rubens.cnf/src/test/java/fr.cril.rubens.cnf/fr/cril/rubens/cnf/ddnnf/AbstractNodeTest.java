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
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractNodeTest {
	
	private Node n1;
	
	private Node n2;
	
	@Before
	public void setUp() {
		this.n1 = new Node(Collections.emptyList());
		final List<Map<Integer, Boolean>> models = Collections.singletonList(Collections.singletonMap(1, true));
		this.n2 = new Node(models);
	}
	
	@Test
	public void testModels() {
		assertEquals(Collections.emptyList(), this.n1.models());
		assertEquals(Collections.singletonList(Collections.singletonMap(1, true)), this.n2.models());
	}
	
	private class Node extends AbstractNode {

		protected Node(final List<Map<Integer, Boolean>> models) {
			super(models);
		}
		
	}

}
