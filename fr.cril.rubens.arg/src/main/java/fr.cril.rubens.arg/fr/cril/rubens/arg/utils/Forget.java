package fr.cril.rubens.arg.utils;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
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
