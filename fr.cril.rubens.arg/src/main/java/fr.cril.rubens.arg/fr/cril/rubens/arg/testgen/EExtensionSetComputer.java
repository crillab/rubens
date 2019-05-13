package fr.cril.rubens.arg.testgen;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	
	/** algorithm for ideal semantics */
	IDEAL_SEM(EExtensionSetComputer::computeIdealExtensions),
	
	/** algorithm for stable semantics */
	STABLE_SEM(EExtensionSetComputer::computeStableExtensions),
	
	/** algorithm for semistable semantics */
	SEMISTABLE_SEM(EExtensionSetComputer::computeSemistableExtensions),
	
	/** algorithm for stage semantics */
	STAGE_SEM(EExtensionSetComputer::computeStageExtensions);
	
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
		final Set<Set<Argument>> extensions = computeConflictFreeExtensions(arguments, attacks).stream().filter(ext -> isComplete(arguments, ext, attacks)).collect(Collectors.toSet());
		return extensions.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector());
	}
	
	private static ExtensionSet computePreferredExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet completeExts = computeCompleteExtensions(arguments, attacks);
		return maxExtensionsForInclusion(completeExts);
	}
	
	private static ExtensionSet computeIdealExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet coExts = computeCompleteExtensions(arguments, attacks);
		final ExtensionSet prExts = maxExtensionsForInclusion(coExts);
		final List<ArgumentSet> prExtsList = prExts.stream().collect(Collectors.toList());
		final Set<Argument> prCommonArgs = prExtsList.get(0).stream().filter(arg -> prExts.stream().skip(1).allMatch(set -> set.contains(arg))).collect(Collectors.toSet());
		final ArgumentSet idealExt = coExts.stream().filter(ext -> ext.stream().allMatch(prCommonArgs::contains)).reduce((e1,e2) -> e1.size() > e2.size() ? e1 : e2).orElseThrow();
		return ExtensionSet.getInstance(Collections.singleton(idealExt));
	}
	
	private static ExtensionSet maxExtensionsForInclusion(final ExtensionSet initExts) {
		final Set<ArgumentSet> argSets = initExts.stream().collect(Collectors.toSet());
		final Set<ArgumentSet> candidates = maxArgSetsForInclusion(argSets);
		return candidates.stream().collect(ExtensionSet.collector());
	}

	private static Set<ArgumentSet> maxArgSetsForInclusion(final Set<ArgumentSet> argSets) {
		final Set<ArgumentSet> candidates = argSets.stream().collect(Collectors.toSet());
		argSets.stream().forEach(ext -> {
			if(candidates.stream().anyMatch(c -> c.isSupersetOf(ext))) {
				candidates.remove(ext);
			}
		});
		return candidates;
	}
	
	private static ExtensionSet computeStableExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		if(arguments.size() == 0) {
			return ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet())));
		}
		final ExtensionSet completeExts = computeCompleteExtensions(arguments, attacks);
		return completeExts.stream().filter(ext -> attacks.rangeOf(ext).size() == arguments.size()).collect(ExtensionSet.collector());
	}
	
	private static ExtensionSet computeSemistableExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet completeExts = computeCompleteExtensions(arguments, attacks);
		return keepMaxRange(completeExts, attacks);
	}

	private static ExtensionSet keepMaxRange(final ExtensionSet extensions, final AttackSet attacks) {
		final Map<ArgumentSet, List<ArgumentSet>> rangeToExts = new HashMap<>();
		extensions.stream().forEach(ext -> rangeToExts.computeIfAbsent(attacks.rangeOf(ext), k -> new ArrayList<>()).add(ext));
		return maxArgSetsForInclusion(rangeToExts.keySet()).stream().map(rangeToExts::get).flatMap(List::stream).collect(ExtensionSet.collector());
	}
	
	private static ExtensionSet computeStageExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		final ExtensionSet cfExts = computeConflictFreeExtensions(arguments, attacks).stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector());
		return keepMaxRange(cfExts, attacks);
	}

	private static Set<Set<Argument>> computeConflictFreeExtensions(final ArgumentSet arguments, final AttackSet attacks) {
		return computeConflictFreeExtensions(arguments.stream().collect(Collectors.toList()), attacks, new HashSet<>(), 0);
	}
	
	private static Set<Set<Argument>> computeConflictFreeExtensions(final List<Argument> arguments, final AttackSet attacks, final Set<Argument> partialExt, final int nextArgIndex) {
		if(nextArgIndex == arguments.size()) {
			return Stream.of(new HashSet<>(partialExt)).collect(Collectors.toSet());
		}
		final Set<Set<Argument>> result = computeConflictFreeExtensions(arguments, attacks, partialExt, nextArgIndex+1);
		final Argument arg = arguments.get(nextArgIndex);
		if(isStillConflictFree(partialExt, attacks, arg)) {
			result.addAll(computeConflictFreeExtensions(arguments, attacks, Stream.concat(partialExt.stream(), Stream.of(arg)).collect(Collectors.toSet()), nextArgIndex+1));
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

	private static boolean isComplete(final ArgumentSet arguments, final Set<Argument> ext, final AttackSet attacks) {
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
					attacks.stream().filter(att -> att.getAttacker().equals(cand)).map(Attack::getAttacked).forEach(a -> {
						defeated.add(a);
						noMoreCandidates.add(a);
					});
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
