package fr.cril.rubens.specs;

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
