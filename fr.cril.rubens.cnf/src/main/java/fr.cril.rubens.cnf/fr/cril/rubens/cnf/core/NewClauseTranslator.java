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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.utils.LiteralUtils;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An {@link InstanceTranslator} dedicated to CNF instances.
 * 
 * Adds a unit clause to a copy of the input instance.
 * The literal of the unit clause is chosen randomly from the set of literals which are not contained in another unit clause.
 * Thus, this translator cannot be applied if there is one unit clause per literal.
 * 
 * The translation process is polynomial in the size of the input instance.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewClauseTranslator implements InstanceTranslator<CnfInstance> {
	
	@Override
	public boolean canBeAppliedTo(final CnfInstance instance) {
		return !instance.models().isEmpty() && instance.clauses().stream().filter(c -> c.size()==1).map(c -> c.get(0)).distinct().count() < (instance.nVars()<<1);
	}

	@Override
	public CnfInstance translate(final CnfInstance instance) {
		int nVars = instance.nVars();
		final List<Integer> litCandidates = new ArrayList<>(nVars);
		for(int i=1; i<=nVars; ++i) {
			litCandidates.add(i);
			litCandidates.add(-i);
		}
		instance.clauses().stream().filter(cl -> cl.size()==1).map(cl -> cl.get(0)).forEach(l -> litCandidates.set(LiteralUtils.dimacsToInternal(l), 0));
		final int lit = LiteralUtils.selectRandomLiteral(litCandidates);
		final List<List<Integer>> newClauses = new ArrayList<>(instance.clauses());
		newClauses.add(Stream.of(lit).collect(Collectors.toList()));
		final int finalLit = lit;
		final List<List<Integer>> newModels = instance.models().stream().filter(m -> m.get(Math.abs(finalLit)-1) == finalLit).collect(Collectors.toList());
		return new CnfInstance(instance.nVars(), newClauses, newModels);
	}

}
