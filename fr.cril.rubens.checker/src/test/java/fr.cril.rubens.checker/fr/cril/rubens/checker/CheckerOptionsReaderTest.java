package fr.cril.rubens.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.checkers.EECOChecker;

public class CheckerOptionsReaderTest {
	
	private CheckerOptionsReader optReader;
	
	private static List<Path> tempFiles = new ArrayList<>();

	@Before
	public void setUp() {
		this.optReader = CheckerOptionsReader.getInstance();
	}
	
	@Test
	public void testDisplayHelp() {
		this.optReader.loadOptions(new String[] {"-h"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	@Test
	public void testDisplayFactories() {
		this.optReader.loadOptions(new String[] {"-l"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTION_EXIT_OK, this.optReader.exitStatus());
	}
	
	@Test
	public void testParseException() {
		this.optReader.loadOptions(new String[] {"-m"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testUnavailableMethod() {
		this.optReader.loadOptions(new String[] {"-m", "toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testOutputToWrongPath() throws IOException {
		this.optReader.loadOptions(new String[] {"-o", "/toto/toto/toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testOutputToRegularFile() throws IOException {
		final Path file = Files.createTempFile("junit-rubens-", null);
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testOutputToUnreadableDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testOutputToUnwritableDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		Files.setPosixFilePermissions(file, Stream.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE).collect(Collectors.toSet()));
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testDefaultMaxDepth() throws IOException {
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO"});
		assertFalse(this.optReader.mustExit());
		assertTrue(this.optReader.getFactory().getClass().equals(EECOChecker.class));
		assertEquals(CheckerOptionsReader.DEFAULT_MAX_DEPTH, this.optReader.getMaxDepth());
	}
	
	@Test
	public void testSetMaxDepth() throws IOException {
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-d", "3"});
		assertFalse(this.optReader.mustExit());
		assertTrue(this.optReader.getFactory().getClass().equals(EECOChecker.class));
		assertEquals(3, this.optReader.getMaxDepth());
	}
	
	@Test
	public void testSetMaxDepthNotInteger() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "toto"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testSetMaxDepthNegativeInteger() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "-1"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testNoMethod() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testNoOutputDirectory() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-m", "CNF"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@AfterClass
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
