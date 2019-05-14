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
 * The node corresponding to the <code>true</code> truth value.
 * 
 * This class implements the singleton design pattern; call {@link TrueNode#getInstance()} to get its instance.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class TrueNode extends AbstractNode {
	
	private static TrueNode instance = null;
	
	/**
	 * Returns the <code>true</code> node.
	 * 
	 * @return the <code>true</code> node
	 */
	public static synchronized TrueNode getInstance() {
		if(instance == null) {
			instance = new TrueNode();
		}
		return instance;
	}
	
	private TrueNode() {
		super(Collections.singletonList(Collections.emptyMap()));
	}

	@Override
	public final boolean equals(final Object obj) {
		return this == obj;
	}

	@Override
	public final int hashCode() {
		return Integer.MAX_VALUE;
	}

}
