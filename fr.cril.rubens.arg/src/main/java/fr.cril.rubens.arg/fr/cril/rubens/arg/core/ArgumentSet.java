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
 * An immutable set of arguments.
 * 
 * There exists at most one instance of this class by argument set.
 * Use {@link ArgumentSet#getInstance(Set)} to get the one corresponding to an argument set.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ArgumentSet implements Comparable<ArgumentSet> {

	/** the set of arguments */
	private final Set<Argument> args;

	/** the id of this set of arguments */
	private final int id;

	/** the id that will be associated with the next {@link ArgumentSet} instance */
	private static int nextId = 0;

	/** the map of all {@link ArgumentSet} instances */
	private static final Map<Set<Argument>, ArgumentSet> ARGSETS = new HashMap<>();

	/**
	 * Builds an {@link ArgumentSet} given the set of arguments and its unique id.
	 * 
	 * @param args the arguments
	 * @param id the id
	 */
	private ArgumentSet(final Set<Argument> args, final int id) {
		this.args = Collections.unmodifiableSet(new HashSet<>(args));
		this.id = id;
	}

	/**
	 * Gets the instance associated with an argument set.
	 * 
	 * If this instance does not exist yet, it is created.
	 * 
	 * @param args the set of arguments
	 * @return the corresponding instance
	 */
	public static ArgumentSet getInstance(final Set<Argument> args) {
		return ARGSETS.computeIfAbsent(args, k -> new ArgumentSet(k, nextId++));
	}

	/**
	 * Gets a stream built upon the arguments.
	 * 
	 * @return a stream built upon the arguments
	 */
	public Stream<Argument> stream() {
		return this.args.stream();
	}

	/**
	 * Returns <code>true</code> iff the provided argument belongs to this set.
	 * 
	 * @param arg the argument
	 * @return <code>true</code> iff the provided argument belongs to this set
	 */
	public boolean contains(final Argument arg) {
		return this.args.contains(arg);
	}
	
	/**
	 * Tells if this argument set is a superset of another one.
	 * 
	 * This is the case if this set's size is strictly greater than the other one's size
	 * and the other set's arguments are all contained by this set.
	 * 
	 * An {@link IllegalArgumentException} exception is raised if the other set is null.
	 * 
	 * @param other the other set
	 * @return <code>true</code> iff this set is a superset of the other
	 */
	public boolean isSupersetOf(final ArgumentSet other) {
		if(other == null) {
			throw new IllegalArgumentException();
		}
		if(this.size() <= other.size()) {
			return false;
		}
		return other.stream().allMatch(this::contains);
	}

	/**
	 * Returns the number of arguments contained in this set.
	 * 
	 * @return the number of arguments contained in this set
	 */
	public int size() {
		return this.args.size();
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
	 * Forget all the known instances of {@link ArgumentSet}.
	 */
	public static void forgetAll() {
		ARGSETS.clear();
	}
	
	/**
	 * Builds a collector building an {@link ArgumentSet} from a stream of arguments.
	 * 
	 * @return a collector for this class
	 */
	public static Collector<Argument, Set<Argument>, ArgumentSet> collector() {
		return new Collector<Argument, Set<Argument>, ArgumentSet>() {
			
			@Override
			public BiConsumer<Set<Argument>, Argument> accumulator() {
				return (acc, elmt) -> acc.add(elmt);
			}

			@Override
			public Set<Characteristics> characteristics() {
				return Stream.of(Characteristics.CONCURRENT, Characteristics.UNORDERED).collect(Collectors.toUnmodifiableSet());
			}

			@Override
			public BinaryOperator<Set<Argument>> combiner() {
				return (a,b) -> {
					a.addAll(b);
					return a;
				};
			}

			@Override
			public Function<Set<Argument>, ArgumentSet> finisher() {
				return ArgumentSet::getInstance;
			}

			@Override
			public Supplier<Set<Argument>> supplier() {
				return HashSet::new;
			}
		};
	}
	
	@Override
	public String toString() {
		return new TreeSet<>(this.args).toString();
	}

	@Override
	public int compareTo(final ArgumentSet other) {
		return this.toString().compareTo(other.toString());
	}
	
}
