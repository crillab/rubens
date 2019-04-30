package fr.cril.rubens.cnf.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import fr.cril.rubens.cnf.utils.CommonOptions;
import fr.cril.rubens.core.Option;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * Common code for checker factories based on satisfiability (and related) problems.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <R> the type of involved instance
 */
@ReflectorParam(enabled=false)
public abstract class ASatCheckerFactory<R extends CnfInstance> implements CheckerFactory<R> {
	
	private final Predicate<R> ignUnsatPred = i -> i.models().isEmpty();
	
	private List<Predicate<R>> ignoredInstanceFilters = new ArrayList<>();
	
	/**
	 * Sets the behavior when UNSAT instances are generated: check against them or ignore.
	 * 
	 * Setting one value or the other does not change the behavior of the instance generator; only the check is ignored.
	 * 
	 * @param value <code>true</code> to ignore the instances
	 */
	public void ignoreUnsat(boolean value) {
		final int index = this.ignoredInstanceFilters.indexOf(ignUnsatPred);
		final int size = this.ignoredInstanceFilters.size();
		if(index != -1) {
			this.ignoredInstanceFilters.set(index, this.ignoredInstanceFilters.get(size-1));
			this.ignoredInstanceFilters.remove(size-1);
		}
		if(value) {
			this.ignoredInstanceFilters.add(ignUnsatPred);
		}
	}
	
	@Override
	public List<Option> getOptions() {
		return CommonOptions.getInstance().getOptions(this);
	}

	@Override
	public boolean ignoreInstance(final R instance) {
		return this.ignoredInstanceFilters.stream().anyMatch(f -> f.test(instance));
	}
	
}
