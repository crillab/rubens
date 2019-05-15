package fr.cril.rubens.cnf.core;

import java.util.ArrayList;
import java.util.List;

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
		final List<List<Integer>> newModels = new ArrayList<>();
		final List<List<Integer>> models = instance.models();
		for(final List<Integer> model : models) {
			final List<Integer> newModel1 = new ArrayList<>(model);
			newModel1.add(-(1+nVars));
			newModels.add(newModel1);
			final List<Integer> newModel2 = new ArrayList<>(model);
			newModel2.add((1+nVars));
			newModels.add(newModel2);
		}
		return new CnfInstance(nVars+1, instance.clauses(), newModels);
	}

}
