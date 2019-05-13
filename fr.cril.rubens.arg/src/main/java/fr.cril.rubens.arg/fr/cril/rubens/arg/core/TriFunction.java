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

/**
 * A function taking three parameters and returning a result.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <A> the type of the first parameter
 * @param <B> the type of the second parameter
 * @param <C> the type of the third parameter
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface TriFunction<A,B,C,R> {
	
	/**
	 * Applies the function.
	 * 
	 * @param a the first parameter
	 * @param b the second parameter
	 * @param c the third parameter
	 * @return the result
	 */
	R apply(A a, B b, C c);

}
