package fr.cril.rubens.cnf.core;

/*-
 * #%L
 * RUBENS module for CNF handling
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
