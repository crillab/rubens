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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A deterministic OR node.
 * 
 * An OR node is deterministic if and only if its children are conflicting (each child implies a different truth value for at least one variable).
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DeterministicOrNode extends AbstractNode {
	
	private final int conflictingVar;
	
	private final INode child1;
	
	private final INode child2;
	
	private final int hash;

	/**
	 * Builds a deterministic OR node given its node index, a variable which different truth values are implies by its two children, and its two children.
	 * 
	 * If the two child nodes are not conflicting, a {@link DDNNFException} exception is thrown.
	 * 
	 * @param nodeIndex the node index
	 * @param conflictingVar a variable which different truth values are implies by its two children
	 * @param child1 the first child node
	 * @param child2 the second child node
	 * @throws DDNNFException if the two child nodes are not conflicting
	 */
	public DeterministicOrNode(final int nodeIndex, final int conflictingVar, final INode child1, final INode child2) throws DDNNFException {
		super(computeModels(nodeIndex, conflictingVar, child1, child2));
		this.conflictingVar = conflictingVar;
		this.child1 = child1;
		this.child2 = child2;
		this.hash = computeHash();
	}

	private int computeHash() {
		return (31 + (this.child1.hashCode() | this.child2.hashCode())) * 31 + this.conflictingVar;
	}

	private static List<Map<Integer, Boolean>> computeModels(final int nodeIndex, final int conflictingVar, final INode child1, final INode child2) throws DDNNFException {
		if(conflictingVar < 1 || child1 == null || child2 == null) {
			throw new IllegalArgumentException();
		}
		checkDeterminism(nodeIndex, conflictingVar, child1, child2);
		return Stream.concat(child1.models().stream(), child2.models().stream()).collect(Collectors.toUnmodifiableList());
	}

	private static void checkDeterminism(final int nodeIndex, final int conflictingVar, final INode child1, final INode child2) throws DDNNFException {
		final List<Map<Integer, Boolean>> models1 = child1.models();
		final List<Map<Integer, Boolean>> models2 = child2.models();
		if(models1.isEmpty() || models2.isEmpty()) {
			return;
		}
		final Boolean value1 = models1.get(0).get(conflictingVar);
		if(value1 == null) {
			throw DDNNFException.newNotDeterministOrNode(nodeIndex);
		}
		final Boolean value2 = !value1;
		if(models1.stream().anyMatch(m -> !value1.equals(m.get(conflictingVar))) || models2.stream().anyMatch(m -> !value2.equals(m.get(conflictingVar)))) {
			throw DDNNFException.newNotDeterministOrNode(nodeIndex);
		}
	}

	@Override
	public final boolean equals(final Object obj) {
		if(!(obj instanceof DeterministicOrNode)) {
			return false;
		}
		final DeterministicOrNode other = (DeterministicOrNode) obj;
		if(this.conflictingVar != other.conflictingVar) {
			return false;
		}
		return (this.child1.equals(other.child1) && this.child2.equals(other.child2)) || (this.child1.equals(other.child2) && this.child2.equals(other.child1));
	}

	@Override
	public final int hashCode() {
		return this.hash;
	}

}
