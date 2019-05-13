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

import java.util.List;
import java.util.Map;

/**
 * Common implementations for dDNNF nodes.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class AbstractNode implements INode {
	
	private final int nodeIndex;
	
	private final List<Map<Integer, Boolean>> models;

	/**
	 * Builds an abstract node given its unique identifier and its set of models.
	 * 
	 * @param nodeIndex the node's unique identifier
	 * @param models the set of models of the formula rooted by this node.
	 */
	protected AbstractNode(final int nodeIndex, final List<Map<Integer, Boolean>> models) {
		this.nodeIndex = nodeIndex;
		this.models = models;
	}

	@Override
	public List<Map<Integer, Boolean>> models() {
		return this.models;
	}

	@Override
	public int hashCode() {
		return this.nodeIndex;
	}

	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof AbstractNode) && this.hashCode() == ((AbstractNode) obj).hashCode();
	}

}
