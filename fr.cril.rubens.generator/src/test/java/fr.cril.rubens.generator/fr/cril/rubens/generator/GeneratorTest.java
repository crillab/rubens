package fr.cril.rubens.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;

public class GeneratorTest {
	
	private static List<Path> tempFiles = new ArrayList<>();
	
	@Test
	public void testNGeneratedCLIArgs() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		final Generator generator = new Generator(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "3"});
		generator.generate();
		final int nExpectedFiles = new CnfInstance().getFileExtensions().size()*generator.getInstanceCount();
		assertEquals(0, generator.getStatusCode());
		assertEquals(nExpectedFiles, Files.list(file).count());
	}
	
	@Test
	public void testNGeneratedProvidedParams() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		final GeneratorOptionsReader optionsReader = GeneratorOptionsReader.getInstance();
		optionsReader.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "3"});
		final Generator generator = new Generator(optionsReader);
		generator.generate();
		final int nExpectedFiles = new CnfInstance().getFileExtensions().size()*generator.getInstanceCount();
		assertEquals(0, generator.getStatusCode());
		assertEquals(nExpectedFiles, Files.list(file).count());
	}
	
	@Test
	public void testCleaning() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		final GeneratorOptionsReader optionsReader1 = GeneratorOptionsReader.getInstance();
		optionsReader1.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "3"});
		final Generator generator1 = new Generator(optionsReader1);
		generator1.generate();
		final GeneratorOptionsReader optionsReader2 = GeneratorOptionsReader.getInstance();
		optionsReader2.loadOptions(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "1"});
		final Generator generator2 = new Generator(optionsReader2);
		generator2.generate();
		assertEquals(new CnfInstance().getFileExtensions().size(), Files.list(file).count());
	}
	
	@Test
	public void testIOException() throws IOException {
		final Path file = Files.createTempDirectory("junit-rubens-");
		tempFiles.add(file);
		final Generator generator = new Generator(new String[] {"-o", file.toString(), "-m", "CNF", "-d", "3"});
		Files.setPosixFilePermissions(file, Collections.emptySet());
		generator.generate();
		assertEquals(Generator.STATUS_IO_EXCEPTION_DURING_GENERATION, generator.getStatusCode());
		generator.generate();
		assertEquals(Generator.STATUS_IO_EXCEPTION_DURING_GENERATION, generator.getStatusCode());
	}
	
	@Test
	public void testWrongArgs() {
		final Generator generator = new Generator(new String[] {});
		generator.generate();
		assertNotEquals(0, generator.getStatusCode());
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
