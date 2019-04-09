package fr.cril.rubens.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.checker.utils.EchoCheckerFactory;
import fr.cril.rubens.reflection.CheckerFactoryReflector;

public class CheckerTest {
	
	private Path tmpDir;

	@Before
	public void setUp() throws IOException {
		CheckerFactoryReflector.getInstance().addClass("ECHO", EchoCheckerFactory.class);
		this.tmpDir = Files.createTempDirectory("rubens-test-");
		EchoCheckerFactory.setAlwaysReturnFalse(false);
	}
	
	@After
	public void tearDown() throws IOException {
		Files.list(this.tmpDir).forEach(arg0 -> {
			try {
				Files.delete(arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Files.delete(this.tmpDir);
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
	
	private boolean checkNoFooBar() throws IOException, InterruptedException {
		final ProcessBuilder pBuilder = new ProcessBuilder(Stream.of("/foo/bar", "--help").collect(Collectors.toList()));
		pBuilder.directory(new File("/foo"));
		try {
			pBuilder.start();
		} catch(IOException e) {
			return true;
		}
		return false;
	}
	
	@Test
	public void testOk() throws IOException, InterruptedException {
		if(!checkCat()) {
			System.out.println("no \"cat\" command; aborting test");
			return;
		}
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", this.tmpDir.toAbsolutePath().toString(), "-d", "3"});
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(0, checker.getErrorCount());
	}
	
	@Test
	public void testWrongCLIOpts() {
		final Checker checker = new Checker(new String[] {"-e", "/bin/cat", "-o", this.tmpDir.toAbsolutePath().toString(), "-d", "3"});
		assertNotEquals(0, checker.getStatusCode());
		checker.check();
		assertEquals(0, checker.getCheckCount());
		assertEquals(0, checker.getErrorCount());
	}
	
	@Test
	public void testHasErrors() throws IOException {
		Files.createFile(Paths.get(this.tmpDir.toAbsolutePath().toString(), "foo.txt"));
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/bin/cat", "-o", this.tmpDir.toAbsolutePath().toString(), "-d", "3"});
		EchoCheckerFactory.setAlwaysReturnFalse(true);
		checker.check();
		assertEquals(7, checker.getCheckCount());
		assertEquals(7, checker.getErrorCount());
		assertEquals(7, Files.list(this.tmpDir).count());
	}
	
	@Test
	public void testExceptionDuringExec() throws IOException, InterruptedException {
		if(!checkNoFooBar()) {
			System.out.println("found \"/foo/bar\" command; aborting test");
			return;
		}
		final Checker checker = new Checker(new String[] {"-m", "ECHO", "-e", "/foo/bar", "-o", this.tmpDir.toAbsolutePath().toString(), "-d", "3"});
		checker.check();
		assertNotEquals(0, checker.getCheckCount());
		assertEquals(checker.getCheckCount(), checker.getErrorCount());
	}
}
