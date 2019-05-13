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
