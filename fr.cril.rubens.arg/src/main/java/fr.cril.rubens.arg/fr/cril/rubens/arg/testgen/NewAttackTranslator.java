package fr.cril.rubens.arg.testgen;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An instance translator that adds new attacks to an existing framework.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewAttackTranslator implements InstanceTranslator<ArgumentationFramework> {
	
	private final EExtensionSetComputer extensionSetComputer;
	
	private boolean autoAttacksAllowed = true;
	
	/**
	 * Builds an instance of this translator given the extension set computer used for the current semantics.
	 * 
	 * @param extensionSetComputer the extension set computer
	 */
	public NewAttackTranslator(final EExtensionSetComputer extensionSetComputer) {
		this.extensionSetComputer = extensionSetComputer;
	}

	@Override
	public boolean canBeAppliedTo(final ArgumentationFramework instance) {
		final int nArgs = instance.getArguments().size();
		return instance.getAttacks().size() < nArgs*nArgs - (this.autoAttacksAllowed ? 0 : nArgs);
	}

	@Override
	public ArgumentationFramework translate(final ArgumentationFramework instance) {
		final Attack newAttack = selectNewAttack(instance);
		return translate(instance, newAttack);
	}
	
	/**
	 * Computes an attack that can be added to the provided AF.
	 * 
	 * You must check that the translator can be applied on the instance before calling this method.
	 * 
	 * @param instance the AF under consideration
	 * @return an attack that can be added to the AF
	 */
	public Attack selectNewAttack(final ArgumentationFramework instance) {
		final List<Argument> args = instance.getArguments().stream().collect(Collectors.toList());
		Collections.shuffle(args);
		for(int i=0; i<args.size(); ++i) {
			final Argument attacker = args.get(i);
			final Set<Argument> alreadyAttacked = instance.getAttacks().stream().filter(att -> att.getAttacker().equals(attacker)).map(Attack::getAttacked).collect(Collectors.toSet());
			if(alreadyAttacked.size() < args.size() - (this.autoAttacksAllowed ? 0 : 1)) {
				final List<Argument> candidateTargets = args.stream().filter(arg -> !alreadyAttacked.contains(arg)).collect(Collectors.toList());
				Collections.shuffle(candidateTargets);
				return Attack.getInstance(attacker, candidateTargets.get(0));
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * Translates an instance by adding an attack.
	 * 
	 * @param instance the instance
	 * @param newAttack the new attack
	 * @return the new instance
	 */
	public ArgumentationFramework translate(final ArgumentationFramework instance, final Attack newAttack) {
		final AttackSet newAttacks = Stream.concat(instance.getAttacks().stream(), Stream.of(newAttack)).collect(AttackSet.collector());
		final ArgumentSet args = instance.getArguments();
		final ExtensionSet newExtensions = this.extensionSetComputer.compute(args, newAttacks);
		final ArgumentationFramework af = new ArgumentationFramework(args, newAttacks, newExtensions, instance, ArgumentationFrameworkTranslation.ArgumentFrameworkAttackTranslation.newAttack(newAttack));
		final List<Argument> argsList = instance.getArguments().stream().collect(Collectors.toList());
		Collections.shuffle(argsList);
		af.setArgUnderDecision(argsList.get(0));
		return af;
	}

	/**
	 * Enable/disable auto-attacks.
	 * 
	 * The default setting is "enabled".
	 * 
	 * @param enabled <code>true</code> to enable, <code>false</code> to disable
	 */
	public void setAutoAttacksAllowed(final boolean enabled) {
		this.autoAttacksAllowed = enabled;
	}
	
	/**
	 * Gets the {@link EExtensionSetComputer} instance used by this translator.
	 * 
	 * @return the {@link EExtensionSetComputer} instance used by this translator
	 */
	public EExtensionSetComputer getExtensionSetComputer() {
		return this.extensionSetComputer;
	}

}
