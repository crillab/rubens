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
		return this.atts.toString();
	}

}
