package fr.cril.rubens.arg.testgen;

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
