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
/**
 * Core types for the RUBENS platform.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
module fr.cril.rubens.core {
	
	exports fr.cril.rubens.core;
	exports fr.cril.rubens.options;
	exports fr.cril.rubens.reflection;
	exports fr.cril.rubens.specs;
	exports fr.cril.rubens.utils;

	requires io.github.lukehutch.fastclasspathscanner;
	requires org.slf4j;
	requires commons.cli;
	
}
