package fr.cril.rubens.cnf.core;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import fr.cril.rubens.cnf.utils.CommonOptions;
import fr.cril.rubens.options.MethodOption;
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
	public List<MethodOption> getOptions() {
		return CommonOptions.getInstance().getOptions(this);
	}

	@Override
	public boolean ignoreInstance(final R instance) {
		return this.ignoredInstanceFilters.stream().anyMatch(f -> f.test(instance));
	}
	
}
