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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import fr.cril.rubens.cnf.utils.LiteralUtils;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An {@link InstanceTranslator} dedicated to CNF instances.
 * 
 * A new literal is added to an existing clause of a copy of the input instance that does not contain it.
 * Thus, this translator cannot be applied if there are no clause, or if every possible clause has already been encoded.
 * The new set of models is computed using Sat4j SAT solver.
 * 
 * The translation process time is exponential in the size of the input instance.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewLitInClauseTranslator implements InstanceTranslator<CnfInstance> {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	@Override
	public boolean canBeAppliedTo(final CnfInstance instance) {
		return !instance.clauses().isEmpty() && instance.clauses().stream().anyMatch(cl -> cl.size() < (instance.nVars() << 1));
	}

	@Override
	public CnfInstance translate(final CnfInstance instance) {
		int clauseIndex = selectClauseIndex(instance);
		final List<List<Integer>> clauses = new ArrayList<>(instance.clauses());
		List<Integer> clause = clauses.get(clauseIndex);
		int lit = selectLiteral(instance, clause);
		int nVars = instance.nVars();
		final List<Integer> newClause = new ArrayList<>(clauses.get(clauseIndex));
		newClause.add(lit);
		final List<List<Integer>> newClauses = new ArrayList<>();
		IntStream.range(0, clauseIndex).boxed().map(clauses::get).forEach(newClauses::add);
		clauses.add(newClause);
		IntStream.range(1+clauseIndex, clauses.size()).boxed().map(clauses::get).forEach(newClauses::add);
		final List<List<Integer>> models = computeModels(nVars, newClauses);
		return new CnfInstance(instance.nVars(), newClauses, models);
	}

	private int selectClauseIndex(final CnfInstance instance) {
		int clauseIndex = -1;
		final List<List<Integer>> initClauses = instance.clauses();
		final List<List<Integer>> clauses = new ArrayList<>(initClauses);
		final int nVars = instance.nVars();
		do {
			final int index = RANDOM.nextInt(clauses.size());
			if(clauses.get(index).size() == (nVars << 1)) {
				clauses.set(index, clauses.get(clauses.size()-1));
				clauses.remove(clauses.size()-1);
			} else {
				clauseIndex = index;
			}
		} while(clauseIndex == -1);
		for(int i=0; i<initClauses.size(); ++i) {
			if(initClauses.get(i).equals(clauses.get(clauseIndex))) {
				return i;
			}
		}
		throw new IllegalArgumentException("clauses are full");
	}
	
	private int selectLiteral(final CnfInstance instance, final List<Integer> clause) {
		final List<Integer> litCandidates = new ArrayList<>(instance.nVars());
		for(int i=1; i<=instance.nVars(); ++i) {
			litCandidates.add(i);
			litCandidates.add(-i);
		}
		clause.stream().map(LiteralUtils::dimacsToInternal).forEach(i -> litCandidates.set(i, 0));
		return LiteralUtils.selectRandomLiteral(litCandidates);
	}

	private List<List<Integer>> computeModels(final int nVars, final List<List<Integer>> clauses) {
		final ISolver solver = SolverFactory.newDefault();
		solver.setTimeoutOnConflicts(Integer.MAX_VALUE);
		solver.newVar(nVars);
		for(final List<Integer> clause : clauses) {
			final int[] clArray = new int[clause.size()];
			for(int i=0; i<clause.size(); ++i) {
				clArray[i] = clause.get(i);
			}
			try {
				solver.addClause(new VecInt(clArray));
			} catch (ContradictionException e) {
				return Collections.emptyList();
			}
		}
		final List<List<Integer>> models = new ArrayList<>();
		final ModelIterator iterator = new ModelIterator(solver);
		try {
			while(iterator.isSatisfiable()) {
				final Set<Integer> model = Arrays.stream(iterator.model()).boxed().collect(Collectors.toSet());
				final List<List<Integer>> fullModels = addFreeVars(model, nVars);
				models.addAll(fullModels);
			}
		} catch (final TimeoutException e) {
			throw new IllegalStateException("an unexpected timeout occurred");
		}
		return models;
	}

	private List<List<Integer>> addFreeVars(final Set<Integer> model, final int nVars) {
		final List<Integer> freeVars = IntStream.range(1, nVars+1).boxed().collect(Collectors.toList());
		model.stream().map(Math::abs).forEach(i -> freeVars.set(i-1, 0));
		int i=0;
		while(i<freeVars.size()) {
			if(freeVars.get(i) == 0) {
				freeVars.set(i, freeVars.get(freeVars.size()-1));
				freeVars.remove(freeVars.size()-1);
			} else {
				++i;
			}
		}
		final List<List<Integer>> models = addFreeVars(new ArrayList<>(model), freeVars, 0);
		Collections.sort(models, (l1,l2) -> {
			assert l1.size() == l2.size();
			for(int j=0; j<l1.size(); ++j) {
				final int cmp = Integer.compare(l1.get(j), l2.get(j));
				if(cmp != 0) {
					return cmp;
				}
			}
			return 0;
		});
		return models;
	}

	private List<List<Integer>> addFreeVars(final List<Integer> model, final List<Integer> freeVars, final int freeVarIndex) {
		if(freeVarIndex == freeVars.size()) {
			return Collections.singletonList(model.stream().sorted((a,b) -> Integer.compare(Math.abs(a), Math.abs(b))).collect(Collectors.toList()));
		}
		final List<List<Integer>> result = new ArrayList<>();
		final List<Integer> model1 = new ArrayList<>(model);
		model1.add(-freeVars.get(freeVarIndex));
		result.addAll(addFreeVars(model1, freeVars, 1+freeVarIndex));
		final List<Integer> model2 = new ArrayList<>(model);
		model2.add(freeVars.get(freeVarIndex));
		result.addAll(addFreeVars(model2, freeVars, 1+freeVarIndex));
		return result;
	}

}
