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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.checkers.ElementaryCheckers.EECOChecker;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

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
		final Map<String, CheckerFactory<Instance>> factories = this.optReader.getFactories();
		assertEquals(1, factories.size());
		assertTrue(factories.entrySet().iterator().next().getValue().getClass().equals(EECOChecker.class));
		assertEquals(CheckerOptionsReader.DEFAULT_MAX_DEPTH, this.optReader.getMaxDepth());
	}
	
	@Test
	public void testSetMaxDepth() throws IOException {
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-d", "3"});
		assertFalse(this.optReader.mustExit());
		final Map<String, CheckerFactory<Instance>> factories = this.optReader.getFactories();
		assertEquals(1, factories.size());
		assertTrue(factories.entrySet().iterator().next().getValue().getClass().equals(EECOChecker.class));
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
		this.optReader.loadOptions(new String[] {"-m", "CNF"});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testNoExecLocation() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-m", "CNF", "-o", file.toString()});
		assertTrue(this.optReader.mustExit());
		assertEquals(CheckerOptionsReader.STATUS_OPTIONS_EXIT_ERROR, this.optReader.exitStatus());
	}
	
	@Test
	public void testNameIsBothAFactoryAndAFactoryCollection() {
		final CheckerFactoryReflector instance = CheckerFactoryReflector.getInstance();
		instance.addClass("ICCMA2019", ACheckerFactory.class);
		this.optReader.setMethod("ICCMA2019");
		boolean mustExit = this.optReader.mustExit();
		instance.resetClasses();
		assertTrue(mustExit);
	}
	
	@Test
	public void testMethodIsACollection() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-o", file.toString()});
		assertFalse(this.optReader.mustExit());
		this.optReader.setMethod("ICCMA2019");
		assertFalse(this.optReader.mustExit());
	}
	
	@Test
	public void testGetExecLoc() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-o", file.toString()});
		assertEquals("foo", this.optReader.getExecLocation());
	}
	
	@Test
	public void testCheckerOptions() {
		this.optReader.setCheckerOptions("a=b;c=d");
		assertEquals("a=b;c=d", this.optReader.getCheckerOptions());
	}
	
	@Test
	public void testGetOutputDir() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		this.optReader.loadOptions(new String[] {"-e", "foo", "-m", "EE-CO", "-o", file.toString()});
		assertEquals(file.toFile(), this.optReader.getOutputDirectory());
	}
	
	@Test
	public void testSetNANMaxDepth() {
		this.optReader.setMaxDepth("foo");
		assertTrue(this.optReader.mustExit());
	}
	
	@Test
	public void testSetNegMaxDepth() {
		this.optReader.setMaxDepth("-1");
		assertTrue(this.optReader.mustExit());
	}
	
	@ReflectorParam(enabled=false)
	private class ACheckerFactory implements CheckerFactory<Instance> {

		@Override
		public void setOptions(String options) {
			// TODO Auto-generated method stub
			
		}

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
