package fr.cril.rubens.reflection;

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

import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A class used to retrieve all the available {@link TestGeneratorFactory} instances.
 * It implements the Singleton design pattern; call {@link TranslatorGeneratorReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link ReflectorParam} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link TestGeneratorFactory} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@SuppressWarnings("rawtypes")
public class TranslatorGeneratorReflector extends AReflector<TestGeneratorFactory> {
	
	/** the instance of {@link TranslatorGeneratorReflector} */
	private static TranslatorGeneratorReflector instance = null;
	
	/**
	 * Returns the (only) instance of the reflector.
	 * @return the instance of the reflector
	 */
	public static TranslatorGeneratorReflector getInstance() {
		if(instance == null) {
			instance = new TranslatorGeneratorReflector();
		}
		return instance;
	}

	private TranslatorGeneratorReflector() {
		super(TestGeneratorFactory.class);
		resetClasses();
	}

}
