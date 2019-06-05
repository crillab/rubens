package fr.cril.rubens.cnf.utils;

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
import java.util.List;

/**
 * Utility class for solver models.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ModelUtils {
	
	private ModelUtils() {
		// nothing to do here; hiding public constructor
	}
	
	/**
	 * Adds a free variable to a list of models.
	 * 
	 * Given a list of models and the number of variables involved in the problem,
	 * builds the list of models resulting from the addition of a new free variables.
	 * The new number of models is thus the double of the initial.
	 * 
	 * The new variable index is equal to the initial number of variables plus one.
	 * 
	 * If the models were sorted in the lexicographic order, the new list of models keeps this property.
	 * 
	 * @param initNVars the initial (i.e. before addition) number of variables
	 * @param models the initial models
	 * @return the new models
	 */
	public static List<List<Integer>> addFreeVarsToModels(final int initNVars, final List<List<Integer>> models) {
		final List<List<Integer>> newModels = new ArrayList<>();
		for(final List<Integer> model : models) {
			final List<Integer> newModel1 = new ArrayList<>(model);
			newModel1.add(-(1+initNVars));
			newModels.add(newModel1);
			final List<Integer> newModel2 = new ArrayList<>(model);
			newModel2.add((1+initNVars));
			newModels.add(newModel2);
		}
		return newModels;
	}

}
