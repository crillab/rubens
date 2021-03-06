package fr.cril.rubens.checker;

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
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.cril.rubens.arg.checking.checkers.ElementaryCheckers.EECOChecker;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

class CheckerOptionsReaderTest {
	
	private CheckerOptionsReader optReader;

	private Path exec;
	
	private static List<Path> tempFiles = new ArrayList<>();

	@BeforeEach
	public void setUp() throws IOException {
		this.optReader = CheckerOptionsReader.getInstance();
		this.exec = Files.createTempFile("junit-rubens-", "", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		tempFiles.add(this.exec);
	}
	
	@Test
	void testDisplayHelp() {
		this.optReader.loadOptions(new String[] {"-h"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	@Test
	void testDisplayFactories() {
		this.optReader.loadOptions(new String[] {"-l"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	private static Stream<Arguments> erroneousArgSets() {
		return Stream.of(
				Arguments.of((Object) new String[] {"-m"}),
				Arguments.of((Object) new String[] {"-o", "/toto/toto/toto"}),
				Arguments.of((Object) new String[] {"-m", "CNF"}),
				Arguments.of((Object) new String[] {"-m", "toto"})
		);
	}
	
	@ParameterizedTest
	@MethodSource("erroneousArgSets")
	void testErroneousArgs(final String[] args) {
		this.optReader.loadOptions(args);
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToRegularFile() throws IOException {
		final Path file = Files.createTempFile("junit-rubens-", null);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToUnreadableDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testOutputToUnwritableDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testDefaultMaxDepth() throws IOException {
		this.optReader.loadOptions(new String[] {"-e", this.exec.toAbsolutePath().toString(), "-m", "EE-CO"});
		assertFalse(this.optReader.mustExit());
		final Map<String, CheckerFactory<Instance>> factories = this.optReader.getFactories();
		assertEquals(1, factories.size());
		assertEquals(factories.entrySet().iterator().next().getValue().getClass(), EECOChecker.class);
		assertEquals(CheckerOptionsReader.DEFAULT_MAX_DEPTH, this.optReader.getMaxDepth());
	}
	
	@Test
	void testSetMaxDepth() throws IOException {
		this.optReader.loadOptions(new String[] {"-e", this.exec.toAbsolutePath().toString(), "-m", "EE-CO", "-d", "3"});
		assertFalse(this.optReader.mustExit());
		final Map<String, CheckerFactory<Instance>> factories = this.optReader.getFactories();
		assertEquals(1, factories.size());
		assertEquals(factories.entrySet().iterator().next().getValue().getClass(), EECOChecker.class);
		assertEquals(3, this.optReader.getMaxDepth());
	}
	
	@Test
	void testSetMaxDepthNotInteger() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testSetMaxDepthNegativeInteger() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "-1"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testNoMethod() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testNoExecLocation() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-m", "CNF", "-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testExecIsADirectory() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		final Path dir = Files.createTempDirectory("junit-rubens-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		tempFiles.add(dir);
		this.optReader.loadOptions(new String[] {"-e", "dir", "-m", "CNF", "-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	void testNameIsBothAFactoryAndAFactoryCollection() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("ICCMA2019", ACheckerFactory.class);
		this.optReader.setMethod("ICCMA2019");
		boolean mustExit = this.optReader.mustExit();
		instance.resetClasses();
		assertTrue(mustExit);
	}
	
	@Test
	void testMethodIsACollection() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", this.exec.toAbsolutePath().toString(), "-m", "EE-CO", "-o", file.toString()});
		assertFalse(this.optReader.mustExit());
		this.optReader.setMethod("ICCMA2019");
		assertFalse(this.optReader.mustExit());
	}
	
	@Test
	void testGetExecLoc() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-o", file.toString()});
		assertEquals("foo", this.optReader.getExecLocation());
	}
	
	@Test
	void testCheckerOptions() {
		this.optReader.setCheckerOptions("a=b;c=d");
		assertEquals("a=b;c=d", this.optReader.getCheckerOptions());
	}
	
	@Test
	void testGetOutputDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-o", file.toString()});
		assertEquals(file.toFile(), this.optReader.getOutputDirectory());
	}
	
	@Test
	void testSetNANMaxDepth() {
		this.optReader.setMaxDepth("foo");
		assertTrue(this.optReader.mustExit());
	}
	
	@Test
	void testSetNegMaxDepth() {
		this.optReader.setMaxDepth("-1");
		assertTrue(this.optReader.mustExit());
	}
	
	@ReflectorParam(enabled=false)
	private class ACheckerFactory implements CheckerFactory<Instance> {

		@Override
		public TestGeneratorFactory<Instance> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<Instance> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(Instance instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<MethodOption> getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean ignoreInstance(Instance instance) {
			// TODO Auto-generated method stub
			return false;
		}
		
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
