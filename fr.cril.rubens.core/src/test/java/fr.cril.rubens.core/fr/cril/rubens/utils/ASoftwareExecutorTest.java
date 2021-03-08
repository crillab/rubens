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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.testutils.StringInstance;

class ASoftwareExecutorTest {
	
	private boolean checkCat() throws IOException, InterruptedException {
		return checkExec(Stream.of("/bin/cat", "--version").collect(Collectors.toList()), "/bin");
	}
	
	private boolean checkTest() throws IOException, InterruptedException {
		return checkExec(Stream.of("/usr/bin/test", "0", "-eq", "0").collect(Collectors.toList()), "/usr/bin");
	}
	
	private boolean checkSleep() throws IOException, InterruptedException {
		return checkExec(Stream.of("/bin/sleep", "1").collect(Collectors.toList()), "/bin");
	}

	private boolean checkExec(final List<String> args, final String pwd) {
		try {
			final ProcessBuilder pBuilder = new ProcessBuilder(args);
			pBuilder.directory(new File(pwd));
			final Process p = pBuilder.start();
			p.getOutputStream().close();
			return p.waitFor() == 0;
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		} catch(IOException e) {
			return false;
		}
	}
	
	@Test
	void testOk() throws IOException, InterruptedException {
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
	void testStatus() throws IOException, InterruptedException {
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
	void testTimeout() throws IOException, InterruptedException {
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
