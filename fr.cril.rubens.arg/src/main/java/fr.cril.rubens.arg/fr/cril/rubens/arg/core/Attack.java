package fr.cril.rubens.arg.core;

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
public class Attack {
	
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

}
