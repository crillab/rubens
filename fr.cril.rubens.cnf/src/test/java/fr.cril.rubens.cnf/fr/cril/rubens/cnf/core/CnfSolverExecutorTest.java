package fr.cril.rubens.cnf.core;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CnfSolverExecutorTest {
	
	@Test
	public void testCLIArgs() {
		final Path execPath = Paths.get("foo", "bar");
		final CnfSolverExecutor<CnfInstance> executor = new CnfSolverExecutor<>(execPath);
		final Map<String, Path> files = new HashMap<>();
		final CnfInstance instance = new CnfInstance();
		final Path cnfPath = Paths.get("foo", "foobar");
		files.put(CnfInstance.CNF_EXT, cnfPath);
		final List<String> args = executor.cliArgs(execPath, files, instance);
		assertEquals(execPath.toAbsolutePath().toString(), args.get(0));
		assertEquals(cnfPath.toAbsolutePath().toString(), args.get(1));
	}

}
