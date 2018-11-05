package fr.cril.rubens.arg.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public class ArgumentSet {

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
		return this.args.toString();
	}
	
}