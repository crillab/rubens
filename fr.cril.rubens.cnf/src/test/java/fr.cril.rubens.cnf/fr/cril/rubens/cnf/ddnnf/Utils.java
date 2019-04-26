package fr.cril.rubens.cnf.ddnnf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	
	private static int nextIndex;
	
	private static int nModels;
	
	private static List<Set<Integer>> models = new ArrayList<>();
	
	public static void resetNextIndex() {
		nextIndex = 0;
	}
	
	public static int nextIndex() {
		return nextIndex++;
	}
	
	public static INode buildXor(final int v1, final int v2) {
		try {
			return new DeterministicOrNode(nextIndex++, v1,
					new DecomposableAndNode(nextIndex++,
							Stream.of(new LiteralNode(nextIndex++, v1), new LiteralNode(nextIndex++, -v2))
									.collect(Collectors.toList())),
					new DecomposableAndNode(nextIndex++,
							Stream.of(new LiteralNode(nextIndex++, -v1), new LiteralNode(nextIndex++, v2))
									.collect(Collectors.toList())));
		} catch (DDNNFException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Map<Integer, Boolean> buildModel(final int... dimacsLits) {
		final Map<Integer, Boolean> result = new TreeMap<>();
		for(int dimacsLit : dimacsLits) {
			result.put(Math.abs(dimacsLit), dimacsLit > 0);
		}
		return result;
	}
	
	public static int countModels(final DDNNF ddnnf) {
		nModels = 0;
		ddnnf.iterateModels(Utils::incModelCounter);
		return nModels;
	}
	
	private static void incModelCounter(final Set<Integer> model) {
		nModels++;
	}
	
	public static List<Set<Integer>> models(final DDNNF ddnnf) {
		models.clear();
		ddnnf.iterateModels(Utils::storeModel);
		return Collections.unmodifiableList(models);
	}
	
	private static void storeModel(final Set<Integer> model) {
		models.add(model);
	}

}
