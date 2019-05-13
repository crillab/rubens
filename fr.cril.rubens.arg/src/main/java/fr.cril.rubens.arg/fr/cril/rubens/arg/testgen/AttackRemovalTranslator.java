package fr.cril.rubens.arg.testgen;

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

import java.util.Random;

import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation.ArgumentFrameworkAttackTranslation;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An instance translator that removes an attack from an existing framework.
 * 
 * The semantics is the complete one.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class AttackRemovalTranslator implements InstanceTranslator<ArgumentationFramework> {
	
	private static final Random RND = new Random();
	
	private final EExtensionSetComputer extensionSetComputer;
	
	/**
	 * Builds an instance of this translator given the extension set computer used for the current semantics.
	 * 
	 * @param extensionSetComputer the extension set computer
	 */
	public AttackRemovalTranslator(final EExtensionSetComputer extensionSetComputer) {
		this.extensionSetComputer = extensionSetComputer;
	}

	@Override
	public boolean canBeAppliedTo(final ArgumentationFramework instance) {
		return instance.getAttacks().size() > 0;
	}

	@Override
	public ArgumentationFramework translate(final ArgumentationFramework instance) {
		final Attack toRemove = selectAttackToRemove(instance);
		return translate(instance, toRemove);
	}
	
	/**
	 * Translates an instance by removing an attack.
	 * 
	 * @param af the instance
	 * @param attack the attack to remove
	 * @return the new instance
	 */
	public ArgumentationFramework translate(final ArgumentationFramework af, final Attack attack) {
		final AttackSet newAttacks = af.getAttacks().stream().filter(a -> !a.equals(attack)).collect(AttackSet.collector());
		final ArgumentSet arguments = af.getArguments();
		final ExtensionSet newExtensions = this.extensionSetComputer.compute(arguments, newAttacks);
		return new ArgumentationFramework(arguments, newAttacks, newExtensions, af, ArgumentFrameworkAttackTranslation.attackRemoval(attack));
	}
	
	/**
	 * Computes an attack that can be removed from the provided AF.
	 * 
	 * You must check that the translator can be applied on the instance before calling this method.
	 * 
	 * @param instance the AF under consideration
	 * @return an attack that can be removed from the AF
	 */
	public Attack selectAttackToRemove(final ArgumentationFramework instance) {
		final AttackSet oldAttacks = instance.getAttacks();
		final int index = RND.nextInt(oldAttacks.size());
		return oldAttacks.stream().skip(index).findFirst().orElseThrow();
	}

}
