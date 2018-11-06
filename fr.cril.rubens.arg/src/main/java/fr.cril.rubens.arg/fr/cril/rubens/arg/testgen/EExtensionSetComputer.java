package fr.cril.rubens.arg.testgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;

/**
 * An enum for extension set computation algorithms.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public enum EExtensionSetComputer {
	
	/** algorithm for complete semantics */
	COMPLETE_SEM(EExtensionSetComputer::computeCompleteExtensions),
	
	/** algorithm for grounded semantics */
	GROUNDED_SEM(EExtensionSetComputer::computeGroundedExtensions),
	
	/** algorithm for preferred semantics */
	PREFERRED_SEM(EExtensionSetComputer::computePreferredExtensions),
	
	/** algorithm for stable semantics */
	STABLE_SEM(EExtensionSetComputer::computeStableExtensions);
	
	private final BiFunction<ArgumentSet, AttackSet, ExtensionSet> computer;
	
	private EExtensionSetComputer(final BiFunction<ArgumentSet, AttackSet, ExtensionSet> computer) {
		this.computer = computer;
	}
	
	/**
	 * Computes the extension of an argumentation framework depicted by its arguments and attacks.
	 * 
	 * @param arguments the AF arguments
	 * @param attacks the AF attacks
	 * @return the extensions
	 */
	public ExtensionSet compute(final ArgumentSet arguments, final AttackSet attacks) {
		return this.computer.apply(arguments, attacks);
	}

	private static ExtensionSet computeCompleteExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final Set<Set<Argument>> extensions = computeCompleteExtensions(arguments.stream().collect(Collectors.toList()), attacks, new HashSet<>(), 0);
		return extensions.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector());
	}
	
	private static ExtensionSet computePreferredExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet completeExts = computeCompleteExtensions(arguments, attacks);
		return maxExtensionsForInclusion(completeExts);
	}
	
	private static ExtensionSet computeStableExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet completeExts = computeCompleteExtensions(arguments, attacks);
		return completeExts.stream().filter(ext -> attacks.rangeOf(ext).size() == arguments.size()).collect(ExtensionSet.collector());
	}
	
	private static ExtensionSet maxExtensionsForInclusion(final ExtensionSet initExts) {
		final Set<ArgumentSet> candidates = initExts.stream().collect(Collectors.toSet());
		initExts.stream().forEach(ext -> {
			if(candidates.stream().anyMatch(c -> c.isSupersetOf(ext))) {
				candidates.remove(ext);
			}
		});
		return candidates.stream().collect(ExtensionSet.collector());
	}

	private static Set<Set<Argument>> computeCompleteExtensions(final List<Argument> arguments, final AttackSet attacks, final Set<Argument> partialExt, final int nextArgIndex) {
		if(nextArgIndex == arguments.size()) {
			return isComplete(arguments, partialExt, attacks) ? Stream.of(new HashSet<>(partialExt)).collect(Collectors.toSet()) : new HashSet<>();
		}
		final Set<Set<Argument>> result = computeCompleteExtensions(arguments, attacks, partialExt, nextArgIndex+1);
		final Argument arg = arguments.get(nextArgIndex);
		if(isStillConflictFree(partialExt, attacks, arg)) {
			result.addAll(computeCompleteExtensions(arguments, attacks, Stream.concat(partialExt.stream(), Stream.of(arg)).collect(Collectors.toSet()), nextArgIndex+1));
		}
		return result;
	}
	
	private static boolean isStillConflictFree(final Set<Argument> partialExt, final AttackSet attacks, final Argument newArg) {
		if(attacks.contains(Attack.getInstance(newArg, newArg))) {
			return false;
		}
		return attacks.stream().noneMatch(att -> attacksPartialExt(partialExt, newArg, att) || attackedByPartialExt(partialExt, newArg, att));
	}
	
	private static boolean attacksPartialExt(final Set<Argument> partialExt, final Argument newArg, final Attack att) {
		return att.getAttacker().equals(newArg) && partialExt.contains(att.getAttacked());
	}

	private static boolean attackedByPartialExt(final Set<Argument> partialExt, final Argument newArg, Attack att) {
		return att.getAttacked().equals(newArg) && partialExt.contains(att.getAttacker());
	}

	private static boolean isComplete(final List<Argument> arguments, final Set<Argument> ext, final AttackSet attacks) {
		final List<Argument> extAttackers = attacks.stream().filter(att -> ext.contains(att.getAttacked())).map(Attack::getAttacker).collect(Collectors.toList());
		final Set<Argument> defeatedByExt = attacks.stream().filter(att -> ext.contains(att.getAttacker())).map(Attack::getAttacked).collect(Collectors.toSet());
		if(extAttackers.stream().anyMatch(a -> !defeatedByExt.contains(a))) {
			return false;
		}
		 return arguments.stream().filter(arg -> !ext.contains(arg)).noneMatch(arg -> attacks.stream().filter(att -> att.getAttacked().equals(arg)).map(Attack::getAttacker).allMatch(defeatedByExt::contains));
	}
	
	private static ExtensionSet computeGroundedExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final Set<Argument> candidates = arguments.stream().collect(Collectors.toSet());
		final Set<Argument> inExt = new HashSet<>();
		final Set<Argument> defeated = new HashSet<>();
		int oldSize;
		do {
			oldSize = inExt.size();
			final List<Argument> noMoreCandidates = new ArrayList<>();
			for(final Argument cand : candidates) {
				final Set<Argument> attackers = attacks.stream().filter(att -> att.getAttacked().equals(cand)).filter(att -> !defeated.contains(att.getAttacker()))
						.map(Attack::getAttacker).collect(Collectors.toSet());
				if(attackers.isEmpty()) {
					inExt.add(cand);
					noMoreCandidates.add(cand);
				} else if(attackers.stream().anyMatch(inExt::contains)) {
					defeated.add(cand);
					noMoreCandidates.add(cand);
				}
			}
			candidates.removeAll(noMoreCandidates);
		} while(inExt.size() > oldSize);
		return ExtensionSet.getInstance(Collections.singleton(inExt.stream().collect(ArgumentSet.collector())));
	}

}
