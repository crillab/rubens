package fr.cril.rubens.utils;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;

/**
 * Some tool methods related to files.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class FileUtils {
	
	private FileUtils() {
		// hiding public constructor
	}
	
	/**
	 * A method used to clean files generating by an old execution of Rubens.
	 * 
	 * Given a directory and some files extensions, this method deletes all the files at the root of the directory which have an extension matching the provided ones.
	 * This method throws no exception: in case an error occurs, a message is displayed using the provided logger.
	 * 
	 * @param directory the directory
	 * @param extensions the file extensions
	 * @param logger the logger
	 */
	public static void cleanOldFiles(final File directory, final Collection<String> extensions, final Logger logger) {
		final File[] folderFiles = directory.listFiles(File::isFile);
		if(folderFiles == null) {
			logger.warn("cannot access directory {} for cleaning", directory);
			return;
		}
		for(final Path path : Arrays.stream(folderFiles).map(File::toURI).map(Paths::get).collect(Collectors.toList())) {
			if(extensions.stream().anyMatch(path.toString()::endsWith)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.warn("cannot clean existing file {}; reason is \"{}\"", path.toAbsolutePath(), e.getMessage());
				}
			}
		}
	}

}
