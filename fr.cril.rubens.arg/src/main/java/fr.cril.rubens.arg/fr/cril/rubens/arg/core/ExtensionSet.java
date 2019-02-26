package fr.cril.rubens.arg.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable set of extensions.
 * 
 * There exists at most one instance of this class by extension set.
 * Use {@link ExtensionSet#getInstance(Set)} to get the one corresponding to an extension.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ExtensionSet {
	
	/** the set of extension */
	private final SortedSet<ArgumentSet> exts;
	
	/** the id of this set of extensions */
	private final int id;
	
	/** the id that will be associated with the next {@link ExtensionSet} instance */
	private static int nextId = 0;
	
	/** the map of all {@link ExtensionSet} instances */
	private static final Map<Set<ArgumentSet>, ExtensionSet> EXTSETS = new HashMap<>();
	
	/**
	 * Builds an {@link ExtensionSet} given the set of extensions and its unique id.
	 * 
	 * @param args the extensions
	 * @param id the id
	 */
	private ExtensionSet(final Set<ArgumentSet> args, final int id) {
		this.exts = Collections.unmodifiableSortedSet(new TreeSet<>(args));
		this.id = id;
	}
	
	/**
	 * Gets the instance associated with an extension set.
	 * 
	 * If this instance does not exist yet, it is created.
	 * 
	 * @param exts the set of extensions
	 * @return the corresponding instance
	 */
	public static synchronized ExtensionSet getInstance(final Set<ArgumentSet> exts) {
		return EXTSETS.computeIfAbsent(exts, k -> new ExtensionSet(k, nextId++));
	}
	
	/**
	 * Gets a stream built upon the extensions.
	 * 
	 * @return a stream built upon the extensions
	 */
	public Stream<ArgumentSet> stream() {
		return this.exts.stream();
	}
	
	/**
	 * Returns <code>true</code> iff this extension set contains no extension.
	 * 
	 * @return <code>true</code> iff this extension set contains no extension
	 */
	public boolean isEmpty() {
		return this.exts.isEmpty();
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
	 * Returns <code>true</code> iff the provided argument set belongs to this extension set.
	 * 
	 * @param argset the argument set
	 * @return <code>true</code> iff the provided argument set belongs to this extension set
	 */
	public boolean contains(final ArgumentSet argset) {
		return this.exts.contains(argset);
	}

	/**
	 * Returns the number of extensions contained in this set.
	 * 
	 * @return the number of extensions contained in this set
	 */
	public int size() {
		return this.exts.size();
	}
	
	/**
	 * Forget all the known instances of {@link ExtensionSet}.
	 */
	public static void forgetAll() {
		EXTSETS.clear();
	}
	
	/**
	 * Builds a collector building an {@link ExtensionSet} from a stream of extensions.
	 * 
	 * @return a collector for this class
	 */
	public static Collector<ArgumentSet, Set<ArgumentSet>, ExtensionSet> collector() {
		return new Collector<ArgumentSet, Set<ArgumentSet>, ExtensionSet>() {
			
			@Override
			public BiConsumer<Set<ArgumentSet>, ArgumentSet> accumulator() {
				return (acc, elmt) -> acc.add(elmt);
			}

			@Override
			public Set<Characteristics> characteristics() {
				return Stream.of(Characteristics.CONCURRENT, Characteristics.UNORDERED).collect(Collectors.toUnmodifiableSet());
			}

			@Override
			public BinaryOperator<Set<ArgumentSet>> combiner() {
				return (a,b) -> {
					a.addAll(b);
					return a;
				};
			}

			@Override
			public Function<Set<ArgumentSet>, ExtensionSet> finisher() {
				return ExtensionSet::getInstance;
			}

			@Override
			public Supplier<Set<ArgumentSet>> supplier() {
				return HashSet::new;
			}
		};
	}
	
	@Override
	public String toString() {
		return this.exts.toString();
	}

}
