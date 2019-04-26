package fr.cril.rubens.cnf.ddnnf;

import java.util.Collections;

/**
 * A leaf node labelled by a literal.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class LiteralNode extends AbstractNode {

	/**
	 * Builds a leaf node given its node index and the dimacs literal that labels it.
	 * 
	 * @param nodeIndex the node index
	 * @param dimacsLiteral the dimacs literal
	 */
	public LiteralNode(final int nodeIndex, final int dimacsLiteral) {
		super(nodeIndex, Collections.singletonList(Collections.singletonMap(Math.abs(dimacsLiteral), dimacsLiteral > 0)));
	}

}
