package fr.cril.rubens.specs;

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * The root interface of all instances.
 * Instances are defined as a couple (problem instance, problem solutions).
 * 
 * Instances may require as many files as they need to be written.
 * To require an instance to write itself, the user must process in the following way:
 * <ol>
 * <li> call the {@link Instance#getFileExtensions()}, </li>
 * <li> build one output stream per extension, </li>
 * <li> for each extension, call {@link Instance#write(String, OutputStream)} with the corresponding output stream.
 * </ol>
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public interface Instance {
	
	/**
	 * Returns the list of file extensions an instance require to write itself.
	 * One extension must be provided by "files" to write, including the problem and the solution(s).
	 * 
	 * @return the list of file extensions an instance require to write itself
	 */
	Collection<String> getFileExtensions();
	
	/**
	 * Writes the content related to the provided extension in the provided {@link OutputStream}.
	 * 
	 * In case the provided extension does not match one returned by {@link Instance#getFileExtensions()},
	 * an {@link IllegalArgumentException} must be thrown.
	 * 
	 * @param extension the extension
	 * @param os the output stream
	 * @throws IllegalArgumentException if an invalid extension is provided
	 * @throws IOException if an I/O error occurs while writing the instance
	 */
	void write(final String extension, OutputStream os) throws IOException;

}
