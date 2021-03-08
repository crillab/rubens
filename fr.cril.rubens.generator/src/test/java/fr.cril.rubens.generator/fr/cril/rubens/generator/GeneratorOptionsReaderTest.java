package fr.cril.rubens.generator;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.cnf.core.CnfTestGeneratorFactory;

class GeneratorOptionsReaderTest {
	
	private static final String TMPFILE_PREFIX = "junit-rubens-";

	private GeneratorOptionsReader optReader;
	
	private static List<Path> tempFiles = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		this.optReader = GeneratorOptionsReader.getInstance();
	}
	
	@Test
	void testDisplayHelp() {
		this.optReader.loadOptions(new String[] {"-h"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	@Test
	void testDisplayFactories() {
		this.optReader.loadOptions(new String[] {"-l"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	@Test
	void testParseException() {
		this.optReader.loadOptions(new String[] {"-m"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testUnavailableMethod() {
		this.optReader.loadOptions(new String[] {"-m", "toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToWrongPath() {
		this.optReader.loadOptions(new String[] {"-o", "/toto/toto/toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToRegularFile() throws IOException {
		final Path file = Files.createTempFile(TMPFILE_PREFIX, null);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToUnreadableDir() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToUnwritableDir() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testDefaultMaxDepth() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF"});
		assertFalse(this.optReader.mustExit());
		assertEquals(file.toString(), this.optReader.getOutputDirectory().getAbsolutePath());
		assertEquals(this.optReader.getFactory().getClass(), CnfTestGeneratorFactory.class);
		assertEquals(GeneratorOptionsReader.DEFAULT_MAX_DEPTH, this.optReader.getMaxDepth());
	}
	
	@Test
	void testSetMaxDepth() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "3"});
		assertFalse(this.optReader.mustExit());
		assertEquals(file.toString(), this.optReader.getOutputDirectory().getAbsolutePath());
		assertEquals(this.optReader.getFactory().getClass(), CnfTestGeneratorFactory.class);
		assertEquals(3, this.optReader.getMaxDepth());
	}
	
	@Test
	void testSetMaxDepthNotInteger() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testSetMaxDepthNegativeInteger() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "-1"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testNoMethod() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testNoOutputDirectory() throws IOException {
		final Path file = Files.createTempDirectory(TMPFILE_PREFIX);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-m", "CNF"});
		assertTrue(this.optReader.mustExit());
		assertEquals(GeneratorOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@AfterAll
	public static void tearDownAfterClass() {
		for(final Path p : tempFiles) {
			try {
				Files.setPosixFilePermissions(p, Stream.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
						PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
				Files.deleteIfExists(p);
			} catch (IOException e) {
				// nothing to do
			}
		}
	}

}
