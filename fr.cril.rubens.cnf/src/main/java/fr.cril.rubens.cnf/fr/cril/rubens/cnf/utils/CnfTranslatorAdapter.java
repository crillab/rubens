package fr.cril.rubens.cnf.utils;

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

import java.lang.reflect.InvocationTargetException;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * An adapter for CNF translators.
 * 
 * As several {@link TestGeneratorFactory} consider instances whose problem is encoded by a CNF formula (but the solutions depend on the problem),
 * the translators can be reused for many kinds of instances.
 * This adapter simply decorate the adapter for types to match to the correct output instance type.
 * 
 * The output instance type may provide a copy constructor taking a {@link CnfInstance} object as parameter.
 * If it is not the case, an {@link IllegalArgumentException} will be raised when calling {@link CnfTranslatorAdapter#translate(CnfInstance)}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <I> the CNF instance input type
 * @param <O> the CNF instance output type
 */
public class CnfTranslatorAdapter<I extends CnfInstance, O extends I> implements InstanceTranslator<O> {
	
	private final InstanceTranslator<I> adaptedTranslator;
	
	public CnfTranslatorAdapter(final InstanceTranslator<I> adaptedTranslator) {
		this.adaptedTranslator = adaptedTranslator;
	}

	@Override
	public boolean canBeAppliedTo(final O instance) {
		return this.adaptedTranslator.canBeAppliedTo(instance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public O translate(O instance) {
		try {
			return (O) instance.getClass().getConstructor(CnfInstance.class).newInstance(this.adaptedTranslator.translate(instance));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Illegal reflection using "+this.getClass().getCanonicalName()
					+ ": you must instantiate a constructor for "+instance.getClass().getCanonicalName()+" taking a "
					+ CnfInstance.class.getCanonicalName()+" instance as parameter.");
		}
	}

}
