package fr.cril.rubens.arg.core;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A class used to handle arguments in argumentation frameworks.
 * 
 * Each argument has a unique name. This class retains only one instance per argument name.
 * Call {@link Argument#getInstance(String)} to get an argument.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class Argument implements Comparable<Argument> {
	
	/** a map used to store all existing arguments */
	private static final Map<String, Argument> ARGUMENTS = new HashMap<>();
	
	/** the (unique) name of the argument */
	private final String name;
	
	/**
	 * Builds an argument given its name.
	 *  
	 * @param name the argument name
	 */
	private Argument(final String name) {
		this.name = name;
	}
	
	/**
	 * Gets the instance of argument matching the provided name.
	 * If such argument does not exist, it is created.
	 * 
	 * The name must be non-null and non-empty.
	 * 
	 * @param name the argument name
	 * @return the corresponding argument
	 */
	public static Argument getInstance(final String name) {
		if(name == null || name.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return ARGUMENTS.computeIfAbsent(name, Argument::new);
	}
	
	/**
	 * Returns the name of the argument.
	 * 
	 * @return the name of the argument
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return this == obj;
	}
	
	/**
	 * Forgets all known arguments.
	 */
	public static void forgetAll() {
		ARGUMENTS.clear();
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(final Argument other) {
		return this.name.compareTo(other.name);
	}

}
