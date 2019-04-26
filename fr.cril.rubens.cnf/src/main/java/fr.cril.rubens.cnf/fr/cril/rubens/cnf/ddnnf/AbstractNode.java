package fr.cril.rubens.cnf.ddnnf;

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
