package fr.cril.rubens.cnf.core;

import java.util.HashSet;
import java.util.Set;

import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An {@link InstanceTranslator} dedicated to CNF instances.
 * 
 * A new free variable is added to a copy of the initial instance.
 * This translator is not applied if the input instance has no model.
 * 
 * The translation process is polynomial in the size of the input instance due to the generation of the models.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewVarTranslator implements InstanceTranslator<CnfInstance> {

	@Override
	public boolean canBeAppliedTo(final CnfInstance instance) {
		return !instance.models().isEmpty();
	}

	@Override
	public CnfInstance translate(final CnfInstance instance) {
		int nVars = instance.nVars();
		final Set<Set<Integer>> newModels = new HashSet<>();
		final Set<Set<Integer>> models = instance.models();
		for(final Set<Integer> model : models) {
			final Set<Integer> newModel1 = new HashSet<>(model);
			newModel1.add(-(1+nVars));
			newModels.add(newModel1);
			final Set<Integer> newModel2 = new HashSet<>(model);
			newModel2.add((1+nVars));
			newModels.add(newModel2);
		}
		return new CnfInstance(nVars+1, instance.clauses(), newModels);
	}

}
