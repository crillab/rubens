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
 * An attack in an argumentation framework.
 * 
 * This class retains only one instance per argument couple.
 * Call {@link Attack#getInstance(Argument, Argument)} to get an argument.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class Attack implements Comparable<Attack> {
	
	/** the arguments that attacks */
	private final Argument attacker;
	
	/** the attacked argument */
	private final Argument attacked;
	
	/** the set of existing attacks, as a mapping attacker -&gt; attacked -&gt; attack */
	private static final Map<Argument, Map<Argument, Attack>> ATTACKS = new HashMap<>();
	
	/**
	 * Builds a new attack given the two involved arguments.
	 * 
	 * @param attacker the arguments that attacks
	 * @param attacked the attacked argument
	 */
	private Attack(final Argument attacker, final Argument attacked) {
		this.attacker = attacker;
		this.attacked = attacked;
	}
	
	/**
	 * Given an attacker and an attacked argument, returns the corresponding attack.
	 * 
	 * If the attacked does not exist yet, it is created and registered for further use.
	 * 
	 * Both arguments must be non-null.
	 * 
	 * @param attacker the attacker
	 * @param attacked the attacked
	 * @return the corresponding attack
	 */
	public static Attack getInstance(final Argument attacker, final Argument attacked) {
		if(attacker == null || attacked == null) {
			throw new IllegalArgumentException();
		}
		return ATTACKS.computeIfAbsent(attacker, k -> new HashMap<>()).computeIfAbsent(attacked, k -> new Attack(attacker, k));
	}
	
	/**
	 * Returns the attacker.
	 * 
	 * @return the attacker
	 */
	public Argument getAttacker() {
		return this.attacker;
	}
	
	/**
	 * Returns the attacked argument.
	 * 
	 * @return the attacked argument
	 */
	public Argument getAttacked() {
		return this.attacked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attacked.hashCode();
		result = prime * result + attacker.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	
	/**
	 * Forgets all known attacks.
	 */
	public static void forgetAll() {
		ATTACKS.clear();
	}
	
	@Override
	public String toString() {
		return "att("+this.attacker+","+this.attacked+")";
	}

	@Override
	public int compareTo(final Attack other) {
		final int cmp = this.attacker.compareTo(other.attacker);
		if(cmp != 0) {
			return cmp;
		}
		return this.attacked.compareTo(other.attacked);
	}

}
