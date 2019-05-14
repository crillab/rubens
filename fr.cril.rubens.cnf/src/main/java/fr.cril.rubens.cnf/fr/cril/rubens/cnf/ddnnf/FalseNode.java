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
 * The node corresponding to the <code>false</code> truth value.
 * 
 * This class implements the singleton design pattern; call {@link FalseNode#getInstance()} to get its instance.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class FalseNode extends AbstractNode {
	
	private static FalseNode instance = null;
	
	/**
	 * Returns the <code>false</code> node.
	 * 
	 * @return the <code>false</code> node
	 */
	public static synchronized FalseNode getInstance() {
		if(instance == null) {
			instance = new FalseNode();
		}
		return instance;
	}

	private FalseNode() {
		super(Collections.emptyList());
	}
	
	@Override
	public final boolean equals(final Object obj) {
		return this == obj;
	}

	@Override
	public final int hashCode() {
		return Integer.MIN_VALUE;
	}

}
