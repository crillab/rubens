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

/**
 * A POJO used to store CLI options specs (names, description, ...).
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class OptionSpecs {
	
	private final String opt;

	private final String longOpt;

	private final boolean hasArg;

	private final String description;
	
	/**
	 * Builds an {@link OptionSpecs} instance given the whole set of specs.
	 * 
	 * @param opt the short name
	 * @param longOpt the long name
	 * @param hasArg <code>true</code> iff the option has an argument
	 * @param description the description
	 */
	public OptionSpecs(final String opt, final String longOpt, final boolean hasArg, final String description) {
		this.opt = opt;
		this.longOpt = longOpt;
		this.hasArg = hasArg;
		this.description = description;
	}

	/**
	 * @return the option short name
	 */
	public String getOpt() {
		return opt;
	}

	/**
	 * @return the option long name
	 */
	public String getLongOpt() {
		return longOpt;
	}

	/**
	 * @return <code>true</code> iff the option has an argument
	 */
	public boolean hasArg() {
		return hasArg;
	}

	/**
	 * @return the option description
	 */
	public String getDescription() {
		return description;
	}

}
