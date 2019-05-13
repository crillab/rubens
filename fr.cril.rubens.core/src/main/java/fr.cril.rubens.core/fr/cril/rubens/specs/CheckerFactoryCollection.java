package fr.cril.rubens.specs;

/*-
 * #%L
 * RUBENS core API
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

import java.util.Set;

import fr.cril.rubens.reflection.ReflectorParam;

/**
 * An interface for collections of {@link CheckerFactory} instances.
 * 
 * This kind of collections allows for example to name series of checking processes.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under test
 */
public interface CheckerFactoryCollection<T extends Instance> {
	
	/**
	 * Returns the names of factories contained in this collection.
	 * 
	 * The names are the ones defined using the {@link ReflectorParam} annotation.
	 * 
	 * @return the names of factories contained in this collection
	 */
	Set<String> getNames();
	
	/**
	 * Returns the factory associated with the provided name.
	 * 
	 * The names of the factories are the ones defined using the {@link ReflectorParam} annotation.
	 * Call {@link CheckerFactoryCollection#getNames()} to get the available factories.
	 * 
	 * @param name the name of the factory
	 * @return the corresponding factory
	 */
	CheckerFactory<T> getFactory(final String name);

}
