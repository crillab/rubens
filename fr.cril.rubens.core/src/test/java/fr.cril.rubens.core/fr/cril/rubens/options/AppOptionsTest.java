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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import fr.cril.rubens.utils.LoggerHelper;

public class AppOptionsTest {
	
	private static TestClass options;
	
	private static boolean f1;
	
	private static boolean f2;
	
	@Before
	public void setUp() {
		options = new TestClass();
		AppOptionsTest.f1 = false;
		AppOptionsTest.f2 = false;
	}
	
	private class TestClass extends AppOptions<TestClass> {

		protected TestClass() {
			super(new TestClassOption[] {
					new TestClassOption("o1", "lo1", false, "d1", (a,b) -> AppOptionsTest.f1 = true),
					new TestClassOption("o2", "lo2", true, "d2", (a,b) -> AppOptionsTest.f2 = true),
					new TestClassOption("o3", "lo3", false, "d3", (a,b) -> AppOptionsTest.options.setMustExit(42))
			});
		}

		@Override
		protected TestClass getThis() {
			return this;
		}

		@Override
		protected void checkOptionsRequirements() {
			// nothing to do here
		}

		@Override
		protected String mandatoryOptionsToString() {
			return "mandatoryOptionsToString";
		}

		@Override
		protected Logger getLogger() {
			return LoggerHelper.getInstance().getLogger();
		}
		
	}
	
	private class TestClassOption implements IAppOption<TestClass> {
		
		private final OptionSpecs specs;
		
		private final BiConsumer<TestClass, String> consumer;

		private TestClassOption(final String opt, final String longOpt, final boolean hasArg, final String description, final BiConsumer<TestClass, String> consumer) {
			this.specs = new OptionSpecs(opt, longOpt, hasArg, description);
			this.consumer = consumer;
		}

		@Override
		public OptionSpecs getSpecs() {
			return this.specs;
		}

		@Override
		public BiConsumer<TestClass, String> getOptionConsumer() {
			return this.consumer;
		}
		
	}
	
	@Test
	public void testDepth() {
		options.setMaxDepth("42");
		assertEquals(42, options.getMaxDepth());
		assertFalse(options.mustExit());
	}
	
	@Test
	public void testNegativeDepth() {
		options.setMaxDepth("-1");
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testNaNDepth() {
		options.setMaxDepth("foo");
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testLoadOptions() {
		options.loadOptions(new String[] {"-o1"});
		assertTrue(AppOptionsTest.f1);
		assertFalse(AppOptionsTest.f2);
		assertFalse(options.mustExit());
	}
	
	@Test
	public void testLoadOptionsParserErr() {
		options.loadOptions(new String[] {"-o2"});
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testExitOption() {
		options.loadOptions(new String[] {"-o3"});
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testSetMustExit() {
		options.setMustExit(42);
		assertTrue(options.mustExit());
		assertEquals(42, options.exitStatus());
	}
	
	@Test
	public void testPrintHelp() {
		options.printHelpAndExit();
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testPrintLicence() {
		options.printLicenseAndExit();
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testReset() {
		final int d = options.getMaxDepth();
		options.setMaxDepth(Integer.toString(d+1));
		options.reset();
		assertEquals(d, options.getMaxDepth());
	}
	
	@Test
	public void testSetOutputDirectoryNew() throws IOException {
		final Path dir0 = Files.createTempDirectory("rubens-test-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		final Path dir = Paths.get(dir0.toAbsolutePath().toString(), "foo");
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		final File out = options.getOutputDirectory();
		Files.deleteIfExists(dir);
		Files.deleteIfExists(dir0);
		assertEquals(dir.toAbsolutePath().toString(), out.getAbsolutePath().toString());
	}
	
	@Test
	public void testSetOutputDirectoryExisting() throws IOException {
		final Path dir = Files.createTempDirectory("rubens-test-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		final File out = options.getOutputDirectory();
		Files.deleteIfExists(dir);
		assertEquals(dir.toAbsolutePath().toString(), out.getAbsolutePath().toString());
	}
	
	@Test
	public void testSetOutputDirectoryExistingCannotRead() throws IOException {
		final Path dir = Files.createTempDirectory("rubens-test-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("-wx------")));
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		Files.deleteIfExists(dir);
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testSetOutputDirectoryExistingCannotWrite() throws IOException {
		final Path dir = Files.createTempDirectory("rubens-test-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("r-x------")));
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		Files.deleteIfExists(dir);
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testSetOutputDirectoryExistingCannotReadNorWrite() throws IOException {
		final Path dir = Files.createTempDirectory("rubens-test-", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("--x------")));
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		Files.deleteIfExists(dir);
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testSetOutputDirectoryExistingRegularFile() throws IOException {
		final Path notADir = Files.createTempFile("rubens-test-", ".tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		options.setOutputDirectory(notADir.toAbsolutePath().toString());
		Files.deleteIfExists(notADir);
		assertTrue(options.mustExit());
	}
	
	@Test
	public void testSetOutputDirectoryCannotCreate() throws IOException {
		final Path dir = Paths.get("/foo/bar/foobar");
		options.setOutputDirectory(dir.toAbsolutePath().toString());
		Files.deleteIfExists(dir);
		assertTrue(options.mustExit());
	}
	
}
