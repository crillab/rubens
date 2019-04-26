package fr.cril.rubens.cnf.ddnnf;

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
		super(nodeIndex, computeModels(nodeIndex, conflictingVar, child1, child2));
	}

	private static List<Map<Integer, Boolean>> computeModels(final int nodeIndex, final int conflictingVar, final INode child1, final INode child2) throws DDNNFException {
		checkDeterminism(nodeIndex, conflictingVar, child1, child2);
		return Stream.concat(child1.models().stream(), child2.models().stream()).collect(Collectors.toUnmodifiableList());
	}

	private static void checkDeterminism(final int nodeIndex, final int conflictingVar, final INode child1, final INode child2) throws DDNNFException {
		final List<Map<Integer, Boolean>> models1 = child1.models();
		final Boolean value1 = models1.get(0).get(conflictingVar);
		if(value1 == null) {
			throw DDNNFException.newNotDeterministOrNode(nodeIndex);
		}
		final Boolean value2 = !value1;
		if(models1.stream().anyMatch(m -> !value1.equals(m.get(conflictingVar))) || child2.models().stream().anyMatch(m -> !value2.equals(m.get(conflictingVar)))) {
			throw DDNNFException.newNotDeterministOrNode(nodeIndex);
		}
	}

}
