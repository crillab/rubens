package fr.cril.rubens.cnf.ddnnf;

import java.util.Collections;

/**
 * The node corresponding to the <code>true</code> truth value.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class TrueNode extends AbstractNode {
	
	/**
	 * Builds a <code>true</code> node with its node index.
	 * 
	 * @param nodeIndex the node index
	 */
	public TrueNode(final int nodeIndex) {
		super(nodeIndex, Collections.singletonList(Collections.emptyMap()));
	}

}
