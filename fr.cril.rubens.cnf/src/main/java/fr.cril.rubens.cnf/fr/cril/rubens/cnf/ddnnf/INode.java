package fr.cril.rubens.cnf.ddnnf;

import java.util.List;
import java.util.Map;

/**
 * An interface for dDNNF nodes.
 * 
 * The nodes must have a unique integer identifier and be able to compute the set of models of the formula they root.
 * 
 * Two equivalent nodes must have the same identifier; two different nodes must have different identifiers.
 * No checks are made for these requirements; not following them is undefined behavior.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public interface INode {
	
	/**
	 * Returns the models of the formula rooted by the node.
	 * 
	 * The models are restricted to the set of variables involved in the subformula.
	 * 
	 * The models are expressed as a list of mappings describing a DNF formula, where each elements of the list acts as a cube.
	 * The cubes are given as maps, where keys are variables and values are the Boolean values assigned to the variables.
	 * 
	 * @return the models of the formula rooted by this node
	 */
	List<Map<Integer, Boolean>> models();
	
}
