package fr.cril.rubens.options;

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

import java.util.function.BiConsumer;

/**
 * An common interface for RUBENS applications command line options.
 * 
 * Each option may have a short name, a long name, a description, and may have an argument.
 * See Apache Commons CLI library for more information.
 * 
 * When an option is present, is it applied through a consumer taking an object and the option argument (if one is present).
 * The type of the object passed to the consumer must be passed to the interface.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of the object passed to the consumer
 */
public interface IAppOption<T> {
	
	/**
	 * Returns the current option short name.
	 * 
	 * @return the current option short name
	 */
	public String getOpt();
	
	/**
	 * Returns the current option long name.
	 * 
	 * @return the current option long name
	 */
	public String getLongOpt();
	
	/**
	 * Returns <code>true</code> iff the options has an argument.
	 * 
	 * @return <code>true</code> iff the options has an argument
	 */
	public boolean hasArg();
	
	/**
	 * Returns the current option description.
	 * 
	 * @return the current option description
	 */
	public String getDescription();
	
	/**
	 * Returns the current option consumer.
	 * 
	 * @return the current option consumer.
	 */
	public BiConsumer<T, String> getOptionConsumer();

}
