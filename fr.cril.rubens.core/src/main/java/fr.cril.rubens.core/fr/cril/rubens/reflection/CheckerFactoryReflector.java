package fr.cril.rubens.reflection;

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

import fr.cril.rubens.specs.CheckerFactory;

/**
 * A class used to retrieve all the available {@link CheckerFactory} instances.
 * It implements the Singleton design pattern; call {@link CheckerFactoryReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link ReflectorParam} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link CheckerFactory} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@SuppressWarnings("rawtypes")
public class CheckerFactoryReflector extends AReflector<CheckerFactory> {
	
	/** the only instance of this reflector */
	private static CheckerFactoryReflector instance = null;

	/**
	 * Returns the (only) instance of the reflector.
	 * 
	 * @return the instance of the reflector
	 */
	public static CheckerFactoryReflector getInstance() {
		if(instance == null) {
			instance = new CheckerFactoryReflector();
		}
		return instance;
	}

	private CheckerFactoryReflector() {
		super(CheckerFactory.class);
		resetClasses();
	}

}
