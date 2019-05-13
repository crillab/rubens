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

import java.util.List;

import fr.cril.rubens.reflection.ReflectorParam;

/**
 * An interface for factories providing all the element needed for a generation process.
 * <ul>
 * <li> {@link TestGeneratorFactory#initInstance()} provides the root instance; </li>
 * <li> {@link TestGeneratorFactory#translators()} provides the list of translators used to generate the instances. </li>
 * </ul>
 * 
 * A {@link TestGeneratorFactory} implementor MUST be parameterized by a {@link ReflectorParam} annotation.
 * The annotation declaration MUST meet at least one of the following requirements:
 * <ul>
 * <li> declare the implementor as disabled (see {@link ReflectorParam#enabled()};</li>
 * <li> give a unique name to the implementor (see {@link ReflectorParam#name()}).</li>
 * </ul>
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under consideration
 */
public interface TestGeneratorFactory<T extends Instance> {
	
	/**
	 * Returns the instance to use as the entry point of the generation algorithm.
	 * 
	 * It is usually a tautological instance.
	 * 
	 * @return the root instance
	 */
	T initInstance();
	
	/**
	 * Returns the list of translators to use in the generation process.
	 * 
	 * A translator may occur multiple times: in this case, it should be applied as times as returned here.
	 * This makes sense in case a generator has a random part in its algorithm.
	 * 
	 * @return the list of translators
	 */
	List<InstanceTranslator<T>> translators();
	
}
