package fr.cril.rubens.arg.core;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable set of attacks.
 * 
 * There exists at most one instance of this class by attack set.
 * Use {@link AttackSet#getInstance(Set)} to get the one corresponding to an attack set.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class AttackSet {
	
	/** the set of attacks */
	private final Set<Attack> atts;
	
	/** the id of this set of attacks */
	private final int id;
	
	/** the id that will be associated with the next {@link AttackSet} instance */
	private static int nextId = 0;
	
	/** the map of all {@link AttackSet} instances */
	private static final Map<Set<Attack>, AttackSet> ATTSETS = new HashMap<>();
	
	/**
	 * Builds an {@link AttackSet} given the set of attacks and its unique id.
	 * 
	 * @param atts the attacks
	 * @param id the id
	 */
	private AttackSet(final Set<Attack> atts, final int id) {
		this.atts = Collections.unmodifiableSet(new HashSet<>(atts));
		this.id = id;
	}
	
	/**
	 * Gets the instance associated with an attack set.
	 * 
	 * If this instance does not exist yet, it is created.
	 * 
	 * @param args the set of attacks
	 * @return the corresponding instance
	 */
	public static AttackSet getInstance(final Set<Attack> args) {
		return ATTSETS.computeIfAbsent(args, k -> new AttackSet(k, nextId++));
	}
	
	/**
	 * Returns the number of attacks contained in this set.
	 * 
	 * @return the number of attacks contained in this set
	 */
	public int size() {
		return this.atts.size();
	}
	
	/**
	 * Gets a stream built upon the attacks.
	 * 
	 * @return a stream built upon the attacks
	 */
	public Stream<Attack> stream() {
		return this.atts.stream();
	}
	
	/**
	 * Returns the range of an extension.
	 * 
	 * The range is defined as the set of arguments composed by the ones of the extension plus the ones attacked by the extension.
	 * 
	 * @param ext the extension
	 * @return the range of the extension
	 */
	public ArgumentSet rangeOf(final ArgumentSet ext) {
		final Set<Argument> attacked = this.atts.stream().filter(att -> ext.contains(att.getAttacker())).map(Attack::getAttacked).collect(Collectors.toSet());
		return Stream.concat(ext.stream(), attacked.stream()).collect(ArgumentSet.collector());
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	
	/**
	 * Returns <code>true</code> iff this attack set contains the attack.
	 * 
	 * @param attack the attack
	 * @return <code>true</code> iff this attack set contains the attack
	 */
	public boolean contains(final Attack attack) {
		return this.atts.contains(attack);
	}
	
	/**
	 * Forget all the known instances of {@link AttackSet}.
	 */
	public static void forgetAll() {
		ATTSETS.clear();
	}
	
	/**
	 * Builds a collector building an {@link AttackSet} from a stream of attacks.
	 * 
	 * @return a collector for this class
	 */
	public static Collector<Attack, Set<Attack>, AttackSet> collector() {
		return new Collector<Attack, Set<Attack>, AttackSet>() {
			
			@Override
			public BiConsumer<Set<Attack>, Attack> accumulator() {
				return (acc, elmt) -> acc.add(elmt);
			}

			@Override
			public Set<Characteristics> characteristics() {
				return Stream.of(Characteristics.CONCURRENT, Characteristics.UNORDERED).collect(Collectors.toUnmodifiableSet());
			}

			@Override
			public BinaryOperator<Set<Attack>> combiner() {
				return (a,b) -> {
					a.addAll(b);
					return a;
				};
			}

			@Override
			public Function<Set<Attack>, AttackSet> finisher() {
				return AttackSet::getInstance;
			}

			@Override
			public Supplier<Set<Attack>> supplier() {
				return HashSet::new;
			}
		};
	}
	
	@Override
	public String toString() {
		return new TreeSet<>(this.atts).toString();
	}

}
