package fr.cril.rubens.arg.semantics.co;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An instance translator that adds new attacks to an existing frameworks.
 * 
 * The semantics is the complete one.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewAttackTranslator implements InstanceTranslator<ArgumentationFramework> {
	
	private boolean autoAttacksAllowed = true;

	@Override
	public boolean canBeAppliedTo(final ArgumentationFramework instance) {
		final int nArgs = instance.getArguments().size();
		return instance.getAttacks().size() < nArgs*nArgs - (this.autoAttacksAllowed ? 0 : nArgs);
	}

	@Override
	public ArgumentationFramework translate(final ArgumentationFramework instance) {
		final List<Argument> args = instance.getArguments().stream().collect(Collectors.toList());
		Collections.shuffle(args);
		for(int i=0; i<args.size(); ++i) {
			final Argument attacker = args.get(i);
			final Set<Argument> alreadyAttacked = instance.getAttacks().stream().filter(att -> att.getAttacker().equals(attacker)).map(Attack::getAttacked).collect(Collectors.toSet());
			if(alreadyAttacked.size() < args.size() - (this.autoAttacksAllowed ? 0 : 1)) {
				final List<Argument> candidateTargets = args.stream().filter(arg -> !alreadyAttacked.contains(arg)).collect(Collectors.toList());
				Collections.shuffle(candidateTargets);
				return translate(instance, Attack.getInstance(attacker, candidateTargets.get(0)));
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
	private ArgumentationFramework translate(final ArgumentationFramework instance, final Attack newAttack) {
		final AttackSet newAttacks = Stream.concat(instance.getAttacks().stream(), Stream.of(newAttack)).collect(AttackSet.collector());
		final ArgumentSet args = instance.getArguments();
		final ExtensionSet newExtensions = computeExtensions(args, newAttacks);
		return new ArgumentationFramework(args, newAttacks, newExtensions);
	}

	/**
	 * Computes the extension of an argumentation framework.
	 * 
	 * @param arguments the AF arguments
	 * @param attacks the AF attacks
	 * @return the extensions
	 */
	private ExtensionSet computeExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final Set<Set<Argument>> extensions = computeExtensions(arguments.stream().collect(Collectors.toList()), attacks, new HashSet<>(), 0);
		return extensions.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector());
	}

	private Set<Set<Argument>> computeExtensions(final List<Argument> arguments, final AttackSet attacks, final Set<Argument> partialExt, final int nextArgIndex) {
		if(nextArgIndex == arguments.size()) {
			return defendsItself(partialExt, attacks) ? Stream.of(new HashSet<>(partialExt)).collect(Collectors.toSet()) : Collections.emptySet();
		}
		final Set<Set<Argument>> result = computeExtensions(arguments, attacks, partialExt, nextArgIndex+1);
		final Argument arg = arguments.get(nextArgIndex);
		if(isStillConflictFree(partialExt, attacks, arg)) {
			result.addAll(computeExtensions(arguments, attacks, Stream.concat(partialExt.stream(), Stream.of(arg)).collect(Collectors.toSet()), nextArgIndex+1));
		}
		return result;
	}
	
	private boolean isStillConflictFree(final Set<Argument> partialExt, final AttackSet attacks, final Argument newArg) {
		if(attacks.contains(Attack.getInstance(newArg, newArg))) {
			return false;
		}
		return attacks.stream().noneMatch(att -> attacksPartialExt(partialExt, newArg, att) || attackedByPartialExt(partialExt, newArg, att));
	}
	
	private boolean attacksPartialExt(final Set<Argument> partialExt, final Argument newArg, final Attack att) {
		return att.getAttacker().equals(newArg) && partialExt.contains(att.getAttacked());
	}

	private boolean attackedByPartialExt(final Set<Argument> partialExt, final Argument newArg, Attack att) {
		return att.getAttacked().equals(newArg) && partialExt.contains(att.getAttacker());
	}

	private boolean defendsItself(final Set<Argument> ext, final AttackSet attacks) {
		final List<Argument> attackers = attacks.stream().filter(att -> ext.contains(att.getAttacked())).map(Attack::getAttacker).collect(Collectors.toList());
		final Set<Argument> defendsAgainst = attacks.stream().filter(att -> ext.contains(att.getAttacker())).map(Attack::getAttacked).collect(Collectors.toSet());
		return attackers.stream().noneMatch(a -> !defendsAgainst.contains(a));
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

}
