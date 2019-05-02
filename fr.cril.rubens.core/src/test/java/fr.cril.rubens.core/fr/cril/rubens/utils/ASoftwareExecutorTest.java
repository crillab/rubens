package fr.cril.rubens.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.testutils.StringInstance;

public class ASoftwareExecutorTest {
	
	private boolean checkCat() throws IOException, InterruptedException {
		final ProcessBuilder pBuilder = new ProcessBuilder(Stream.of("/bin/cat", "--version").collect(Collectors.toList()));
		pBuilder.directory(new File("/bin"));
		final Process p = pBuilder.start();
		p.getOutputStream().close();
		return p.waitFor() == 0;
	}
	
	private boolean checkTest() throws IOException, InterruptedException {
		final ProcessBuilder pBuilder = new ProcessBuilder(Stream.of("/usr/bin/test", "0", "-eq", "0").collect(Collectors.toList()));
		pBuilder.directory(new File("/usr/bin"));
		final Process p = pBuilder.start();
		p.getOutputStream().close();
		return p.waitFor() == 0;
	}
	
	private boolean checkSleep() throws IOException, InterruptedException {
		final ProcessBuilder pBuilder = new ProcessBuilder(Stream.of("/bin/sleep", "1").collect(Collectors.toList()));
		pBuilder.directory(new File("/bin"));
		final Process p = pBuilder.start();
		p.getOutputStream().close();
		return p.waitFor() == 0;
	}
	
	@Test
	public void testOk() throws IOException, InterruptedException {
		if(!checkCat()) {
			return;
		}
		final StringInstanceSoftwareExecutor executor = new StringInstanceSoftwareExecutor();
		final StringInstance instance = new StringInstance("foo");
		SoftwareExecutorResult result = executor.exec(instance);
		assertEquals(0, result.getStatus());
		assertEquals(instance.str()+"\n", result.getStdout());
		assertEquals("", result.getStderr());
	}
	
	@Test
	public void testStatus() throws IOException, InterruptedException {
		if(!checkTest()) {
			return;
		}
		final PositiveStatusSoftwareExecutor executor = new PositiveStatusSoftwareExecutor();
		final StringInstance instance = new StringInstance("foo");
		SoftwareExecutorResult result = executor.exec(instance);
		assertNotEquals(0, result.getStatus());
		assertEquals("", result.getStdout());
		assertEquals("", result.getStderr());
	}
	
	@Test
	public void testTimeout() throws IOException, InterruptedException {
		if(!checkSleep()) {
			return;
		}
		final SleepingSoftwareExecutor executor = new SleepingSoftwareExecutor();
		executor.setTimeout(1, TimeUnit.MICROSECONDS);
		final StringInstance instance = new StringInstance("foo");
		SoftwareExecutorResult result = executor.exec(instance);
		assertTrue(result.hasTimeouted());
	}
	
	private class StringInstanceSoftwareExecutor extends ASoftwareExecutor<StringInstance> {
		
		private StringInstanceSoftwareExecutor() {
			super(Paths.get("/bin/cat"));
		}

		@Override
		protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final StringInstance instance) {
			return Stream.of(execLocation.toAbsolutePath().toString(), instanceFiles.get(".str").toAbsolutePath().toString()).collect(Collectors.toList());
		}
		
	}
	
	private class PositiveStatusSoftwareExecutor extends ASoftwareExecutor<Instance> {
		
		public PositiveStatusSoftwareExecutor() {
			super(Paths.get("/usr/bin/test"));
		}

		@Override
		protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final Instance instance) {
			return Stream.of("/usr/bin/test", "0", "-eq", "1").collect(Collectors.toList());
		}
		
	}
	
	private class SleepingSoftwareExecutor extends ASoftwareExecutor<Instance> {
		
		public SleepingSoftwareExecutor() {
			super(Paths.get("/bin/sleep"));
		}

		@Override
		protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final Instance instance) {
			return Stream.of("/bin/sleep", "1").collect(Collectors.toList());
		}
		
	}

}