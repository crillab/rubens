package fr.cril.rubens.cnf.core;

/*-
 * #%L
 * RUBENS module for CNF handling
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
