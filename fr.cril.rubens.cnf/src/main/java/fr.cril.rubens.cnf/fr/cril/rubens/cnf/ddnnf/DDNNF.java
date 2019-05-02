package fr.cril.rubens.cnf.ddnnf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A dDNNF, i.e. a NNF with decomposable AND nodes and deterministic OR nodes.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DDNNF {
	
	private final int nVars;
	
	private final INode root;
	
	/**
	 * Builds a dDNNF with no constraints.
	 * 
	 * The models are all the possible instantiations of the variables.
	 * 
	 * @param nVars the number of variables
	 */
	public DDNNF(final int nVars) {
		this(nVars, new TrueNode(0));
	}

	/**
	 * Builds a dDNNF given the number of variables and its root node.
	 * 
	 * The number of variables may be higher than the one considered by the root node.
	 * In this case, these additional variables are let free: the models of the root are then completed
	 * by all possible instantiations of the free variables.
	 *  
	 * @param nVars the number of variables
	 * @param root the root node
	 */
	public DDNNF(final int nVars, final INode root) {
		this.nVars = nVars;
		this.root = root;
	}
	
	/**
	 * Iterates over the models of the dDNNF and send them to the provided {@link Consumer}.
	 * 
	 * @param modelConsumer the model consumer
	 */
	public void iterateModels(final Consumer<Set<Integer>> modelConsumer) {
		if(this.nVars == 0) {
			modelConsumer.accept(Collections.emptySet());
			return;
		}
		for(Map<Integer, Boolean> rootModel : this.root.models()) {
			final Set<Integer> model = rootModel.entrySet().stream().map(e -> e.getValue() ? e.getKey() : -e.getKey()).collect(Collectors.toSet());
			if(model.size() == this.nVars) {
				modelConsumer.accept(model);
			} else {
				iterateOverFulfilledModels(model, modelConsumer);
			}
		}
	}

	private void iterateOverFulfilledModels(final Set<Integer> model, final Consumer<Set<Integer>> modelConsumer) {
		boolean[] missing = new boolean[this.nVars];
		Arrays.fill(missing, true);
		model.stream().map(Math::abs).forEach(i -> missing[i-1] = false);
		final List<Integer> missingList = IntStream.range(0, this.nVars).filter(i -> missing[i]).map(i -> i+1).boxed().collect(Collectors.toList());
		iterateOverFulfilledModels(model, modelConsumer, missingList, 0, new TreeSet<>());
	}

	private void iterateOverFulfilledModels(final Set<Integer> model, final Consumer<Set<Integer>> modelConsumer, final List<Integer> missing, final int nextMissing, Set<Integer> currentFulfilled) {
		if(nextMissing == missing.size()) {
			currentFulfilled.addAll(model);
			modelConsumer.accept(currentFulfilled);
			return;
		}
		final Set<Integer> cp1 = new TreeSet<>(currentFulfilled);
		cp1.add(-missing.get(nextMissing));
		iterateOverFulfilledModels(model, modelConsumer, missing, nextMissing + 1, cp1);
		final Set<Integer> cp2 = new TreeSet<>(currentFulfilled);
		cp2.add(missing.get(nextMissing));
		iterateOverFulfilledModels(model, modelConsumer, missing, nextMissing + 1, cp2);
	}

}
