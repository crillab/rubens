package fr.cril.rubens.arg.core;

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

import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * An interface for {@link CheckerFactory} instances dedicated to argumentation frameworks.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <R> the type of instances under consideration
 */
public interface ArgumentationFrameworkCheckerFactory<R extends AArgumentationFrameworkGraph> extends CheckerFactory<R> {
	
	/**
	 * Sets the solver output decoder.
	 * 
	 * @param decoder the decoder
	 */
	void setOutputFormat(ISolverOutputDecoder decoder);

	@Override
	default boolean ignoreInstance(final R instance) {
		return false;
	}

}
