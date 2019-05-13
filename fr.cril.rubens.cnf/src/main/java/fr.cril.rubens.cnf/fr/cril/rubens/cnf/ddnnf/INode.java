package fr.cril.rubens.cnf.ddnnf;

/*-
 * #%L
 * RUBENS module for CNF handling
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
