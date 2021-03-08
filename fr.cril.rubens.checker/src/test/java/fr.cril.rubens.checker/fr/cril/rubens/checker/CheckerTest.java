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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.cril.rubens.checker.utils.EchoCheckerFactory;
import fr.cril.rubens.reflection.CheckerFactoryReflector;

class CheckerTest {
	
	private static Path tmpDir;

	@BeforeEach
	public void setUp() throws IOException {
		CheckerFactoryReflector.getInstance().addClass("ECHO", EchoCheckerFactory.class);
		tmpDir = Files.createTempDirectory("rubens-test-");
		EchoCheckerFactory.setAlwaysReturnFalse(false);
	}
	
	@AfterEach
	public void tearDown() throws IOException {
		Files.list(tmpDir).forEach(arg0 -> {
			try {
				Files.delete(arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Files.delete(tmpDir);
		CheckerFactoryReflector.getInstance().resetClasses();
		EchoCheckerFactory.setAlwaysReturnFalse(false);
	}
	
	private boolean checkCat() throws IOException, InterruptedException {
		final ProcessBuilder pBuilder = new ProcessBuilder(Stream.of("/bin/cat", "--version").collect(Collectors.toList()));
		pBuilder.directory(new File("/bin"));
		final Process p = pBuilder.start();
		p.getOutputStream().close();
		return p.waitFor() == 0;
	}
	
	@Test
	void testOk() throws IOException, InterruptedException {
		if(!checkCat()) {
			System.out.println("no \"cat\" command; aborting test");
			return;
		}
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(0, checker.getErrorCount());
	}
	
	@Test
	void testWrongCLIOpts() {
		final Checker checker = new Checker(new String[] {"-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		assertNotEquals(0, checker.getStatusCode());
		checker.check();
		assertEquals(0, checker.getCheckCount());
		assertEquals(0, checker.getErrorCount());
	}
	
	@Test
	void testHasErrors() throws IOException {
		Files.createFile(Paths.get(tmpDir.toAbsolutePath().toString(), "foo.txt"));
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(7, checker.getErrorCount());
		assertEquals(7, Files.list(tmpDir).count());
	}
	
	@Test
	void testHasErrorsWithoutOutputDir() throws IOException {
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(7, checker.getErrorCount());
	}
	
	@Test
	void testHasAFileNotToClean() throws IOException {
		Files.createFile(Paths.get(tmpDir.toAbsolutePath().toString(), "foo.txt"));
		Files.createFile(Paths.get(tmpDir.toAbsolutePath().toString(), "foo.bar"));
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		checker.check();
		assertEquals(8, Files.list(tmpDir).count());
	}
	
	@Test
	void testHasErrorsButNoOutputDir() throws IOException {
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(7, checker.getErrorCount());
	}
	
	@Test
	void testCannotReadOutputDir() throws IOException {
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		final Set<PosixFilePermission> oldPerms = Files.getPosixFilePermissions(tmpDir);
		Files.setPosixFilePermissions(tmpDir, PosixFilePermissions.fromString("---------"));
		checker.check();
		Files.setPosixFilePermissions(tmpDir, oldPerms);
		assertEquals(7, checker.getCheckCount());
		assertEquals(7, checker.getErrorCount());
		assertEquals(0, Files.list(tmpDir).count());
	}
	
	@Test
	void testExceptionDuringExec() throws IOException, InterruptedException {
		final Path path = Files.createTempFile("junit-rubens-", "", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", path.toAbsolutePath().toString(), "-o", tmpDir.toAbsolutePath().toString(), "-d", "3"});
		Files.delete(path);
		checker.check();
		assertNotEquals(0, checker.getCheckCount());
		assertEquals(checker.getCheckCount(), checker.getErrorCount());
	}
	
	@ParameterizedTest
	@CsvSource({
		"ignAll=true, 0, 0, 7",
		"foo=bar, 7, 0, 0",
		"foobar, 7, 0, 0"
	})
	void testCounts(final String finalArg, final int checkCount, final int errCount, final int ignCount) throws IOException, InterruptedException {
		if(!checkCat()) {
			System.out.println("no \"cat\" command; aborting test");
			return;
		}
		String[] args = new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", tmpDir.toAbsolutePath().toString(), "-d", "3", "-c", "ignAll=true"};
		args[args.length-1] = finalArg;
		final Checker checker = new Checker(args);
		checker.check();
		assertEquals(checkCount, checker.getCheckCount());
		assertEquals(errCount, checker.getErrorCount());
		assertEquals(ignCount, checker.getIgnoredCount());
	}
	
	@Test
	void testHelp() throws IOException, InterruptedException {
		final Checker checker = new Checker(new String[] {"-h"});
		checker.check();
		assertEquals(0, checker.getCheckCount());
	}
}
