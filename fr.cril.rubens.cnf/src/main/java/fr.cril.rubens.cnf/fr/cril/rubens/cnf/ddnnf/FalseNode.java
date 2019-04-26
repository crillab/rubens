package fr.cril.rubens.cnf.ddnnf;

import java.util.Collections;

/**
 * The node corresponding to the <code>false</code> truth value.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class FalseNode extends AbstractNode {

	/**
	 * Builds a <code>false</code> node with its node index.
	 * 
	 * @param nodeIndex the node index
	 */
	public FalseNode(final int nodeIndex) {
		super(nodeIndex, Collections.emptyList());
	}

}
