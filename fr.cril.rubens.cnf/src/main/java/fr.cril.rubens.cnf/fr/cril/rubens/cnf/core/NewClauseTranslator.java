package fr.cril.rubens.cnf.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
		final Set<Set<Integer>> newModels = instance.models().stream().filter(m -> m.contains(finalLit)).collect(Collectors.toSet());
		return new CnfInstance(instance.nVars(), newClauses, newModels);
	}

}
