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
