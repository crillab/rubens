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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class FileUtilsTest {
	
	@Test
	public void testCleanOldFiles() throws IOException {
		final Path tempDirectory = Files.createTempDirectory("rubens-test-");
		final Path f1 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.a"));
		final Path f2 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.b"));
		FileUtils.cleanOldFiles(tempDirectory.toFile(), Stream.of(".a").collect(Collectors.toList()), LoggerHelper.getInstance().getLogger());
		final List<Path> files = Files.list(tempDirectory).collect(Collectors.toList());
		Files.deleteIfExists(f2);
		Files.deleteIfExists(f1);
		Files.deleteIfExists(tempDirectory);
		assertEquals(1, files.size());
		assertTrue(files.get(0).endsWith("foo.b"));
	}
	
	@Test
	public void testNotADir() throws IOException {
		final Path tempDirectory = Files.createTempDirectory("rubens-test-");
		final Path f1 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.a"));
		final Path f2 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.b"));
		FileUtils.cleanOldFiles(f1.toFile(), Stream.of(".a").collect(Collectors.toList()), LoggerHelper.getInstance().getLogger());
		final List<Path> files = Files.list(tempDirectory).collect(Collectors.toList());
		Files.deleteIfExists(f2);
		Files.deleteIfExists(f1);
		Files.deleteIfExists(tempDirectory);
		assertEquals(2, files.size());
	}
	
	@Test
	public void testCannotReadNorDelete() throws IOException {
		final Path tempDirectory = Files.createTempDirectory("rubens-test-");
		final Path f1 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.a"));
		final Path f2 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.b"));
		Files.setPosixFilePermissions(tempDirectory, PosixFilePermissions.fromString("---------"));
		FileUtils.cleanOldFiles(tempDirectory.toFile(), Stream.of(".a").collect(Collectors.toList()), LoggerHelper.getInstance().getLogger());
		Files.setPosixFilePermissions(tempDirectory, PosixFilePermissions.fromString("rwx------"));
		final List<Path> files = Files.list(tempDirectory).collect(Collectors.toList());
		Files.deleteIfExists(f2);
		Files.deleteIfExists(f1);
		Files.deleteIfExists(tempDirectory);
		assertEquals(2, files.size());
	}
	
	@Test
	public void testCannotDelete() throws IOException {
		final Path tempDirectory = Files.createTempDirectory("rubens-test-");
		final Path f1 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.a"));
		final Path f2 = Files.createFile(Paths.get(tempDirectory.toAbsolutePath().toString(), "foo.b"));
		Files.setPosixFilePermissions(tempDirectory, PosixFilePermissions.fromString("r-x------"));
		FileUtils.cleanOldFiles(tempDirectory.toFile(), Stream.of(".a").collect(Collectors.toList()), LoggerHelper.getInstance().getLogger());
		Files.setPosixFilePermissions(tempDirectory, PosixFilePermissions.fromString("rwx------"));
		final List<Path> files = Files.list(tempDirectory).collect(Collectors.toList());
		Files.deleteIfExists(f2);
		Files.deleteIfExists(f1);
		Files.deleteIfExists(tempDirectory);
		assertEquals(2, files.size());
	}

}
