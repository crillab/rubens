package fr.cril.rubens.arg.testgen;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
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

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A {@link TestGeneratorFactory} instance used to build abstract argumentation problems for the Ideal semantics.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="ARG-ID", family="AF")
public class IdealSemTestGeneratorFactory extends ASemTestGeneratorFactory {

	/**
	 * Builds a new factory for this semantics with the empty instance used as the root test generation instance.
	 */
	public IdealSemTestGeneratorFactory() {
		this(true);
	}

	/**
	 * Builds a new factory for this semantics.
	 * 
	 * A flag is used to allow/disallow the empty argumentation framework.
	 * In case it is allowed, it is the root instance.
	 * In the other case, the root instance is the one having a single argument and no attacks.
	 * 
	 * @param emptyInstanceAllowed the flag used to allow/disallow the empty argumentation framework
	 */
	public IdealSemTestGeneratorFactory(final boolean emptyInstanceAllowed) {
		super(EExtensionSetComputer.IDEAL_SEM, emptyInstanceAllowed);
	}

}
