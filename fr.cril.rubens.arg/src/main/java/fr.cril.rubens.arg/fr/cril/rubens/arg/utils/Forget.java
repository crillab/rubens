package fr.cril.rubens.arg.utils;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;

/**
 * A utility class used to forget cached objects.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class Forget {
	
	private Forget() {
		// nothing to do here
	}

	/**
	 * Forgets all cached objects in the RUBENS argumentation module.
	 */
	public static void all() {
		ExtensionSet.forgetAll();
		ArgumentSet.forgetAll();
		Argument.forgetAll();
		AttackSet.forgetAll();
		Attack.forgetAll();
	}

}
