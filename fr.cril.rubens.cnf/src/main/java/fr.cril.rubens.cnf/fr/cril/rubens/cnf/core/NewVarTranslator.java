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

import java.util.List;

import fr.cril.rubens.cnf.utils.ModelUtils;
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
		final int nVars = instance.nVars();
		final List<List<Integer>> newModels = ModelUtils.addFreeVarsToModels(nVars, instance.models());
		return new CnfInstance(nVars+1, instance.clauses(), newModels);
	}

}
