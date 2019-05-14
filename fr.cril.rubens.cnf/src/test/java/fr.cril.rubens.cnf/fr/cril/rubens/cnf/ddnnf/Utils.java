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
			final DecomposableAndNode and1 = new DecomposableAndNode(nextIndex++, Stream.of(new LiteralNode(v1), new LiteralNode(-v2)).collect(Collectors.toList()));
			nextIndex += 2;
			final DecomposableAndNode and2 = new DecomposableAndNode(nextIndex++, Stream.of(new LiteralNode(-v1), new LiteralNode(v2)).collect(Collectors.toList()));
			nextIndex += 2;
			return new DeterministicOrNode(nextIndex++, v1, and1, and2);
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
