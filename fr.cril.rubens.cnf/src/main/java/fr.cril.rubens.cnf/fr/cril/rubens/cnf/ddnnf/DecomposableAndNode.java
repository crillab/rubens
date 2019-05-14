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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A decomposable AND nodes.
 * 
 * A AND node is decomposable if its children share no variables.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DecomposableAndNode extends AbstractNode {
	
	private final List<INode> children;
	
	private final int hash;

	/**
	 * Builds a decomposable AND nodes given its node index and its children.
	 * 
	 * There must be at least one child node (an {@link IllegalArgumentException} is throw in the other case).
	 * 
	 * The decomposability property is checked. If the node does not satisfy it, a {@link DDNNFException} is thrown.
	 * 
	 * @param nodeIndex the node index
	 * @param children the node's children
	 * @throws DDNNFException if the node is not decomposable
	 */
	public DecomposableAndNode(final int nodeIndex, final List<INode> children) throws DDNNFException {
		super(computeModels(nodeIndex, children));
		this.children = children;
		this.hash = computeHash();
	}

	private int computeHash() {
		return this.children.stream().mapToInt(Object::hashCode).reduce(0, (a,b) -> a|b);
	}

	private static List<Map<Integer, Boolean>> computeModels(final int nodeIndex, final List<INode> children) throws DDNNFException {
		if(children == null || children.isEmpty() || children.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException();
		}
		checkDecomposability(nodeIndex, children);
		List<Map<Integer, Boolean>> currentModels = children.get(0).models();
		for(int i=1; i<children.size(); ++i) {
			currentModels = applyAnd(currentModels, children.get(i).models());
		}
		return Collections.unmodifiableList(currentModels);
	}

	private static void checkDecomposability(final int nodeIndex, final List<INode> children) throws DDNNFException {
		for(int i=0; i<children.size()-1; ++i) {
			for(int j=i+1; j<children.size(); ++j) {
				final List<Integer> vars1 = children.get(i).models().stream().map(Map::keySet).flatMap(Set::stream).collect(Collectors.toList());
				final Set<Integer> vars2 = children.get(j).models().stream().map(Map::keySet).flatMap(Set::stream).collect(Collectors.toSet());
				if(vars1.stream().anyMatch(vars2::contains)) {
					throw DDNNFException.newNotDecomposableAndNode(nodeIndex);
				}
			}
		}
	}

	private static List<Map<Integer, Boolean>> applyAnd(final List<Map<Integer, Boolean>> models, final List<Map<Integer, Boolean>> otherModels) {
		final List<Map<Integer, Boolean>> result = new ArrayList<>();
		for(final Map<Integer, Boolean> mod : models) {
			for(final Map<Integer, Boolean> otherMod : otherModels) {
				final Map<Integer, Boolean> conjunction = applyAnd(mod, otherMod);
				result.add(conjunction);
			}
		}
		return result;
	}

	private static Map<Integer, Boolean> applyAnd(final Map<Integer, Boolean> mod1, final Map<Integer, Boolean> mod2) {
		final Map<Integer, Boolean> result = new TreeMap<>();
		mod1.entrySet().stream().forEach(e -> result.put(e.getKey(), e.getValue()));
		mod2.entrySet().stream().forEach(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}
	
	@Override
	public final boolean equals(final Object obj) {
		if(!(obj instanceof DecomposableAndNode)) {
			return false;
		}
		final DecomposableAndNode other = (DecomposableAndNode) obj;
		return new HashSet<>(this.children).equals(new HashSet<>(other.children));
	}

	@Override
	public final int hashCode() {
		return this.hash;
	}

}
