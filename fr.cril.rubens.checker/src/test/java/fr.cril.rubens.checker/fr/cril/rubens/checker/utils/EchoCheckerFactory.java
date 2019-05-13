package fr.cril.rubens.checker.utils;

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

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(enabled=false)
public class EchoCheckerFactory implements CheckerFactory<EchoInstance> {
	
	private static boolean alwaysReturnFalse = false;
	
	private boolean ignoreAll = false;

	@Override
	public TestGeneratorFactory<EchoInstance> newTestGenerator() {
		return new EchoTestGenerator();
	}

	@Override
	public ASoftwareExecutor<EchoInstance> newExecutor(final Path execPath) {
		return new EchoExecutor(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final EchoInstance instance, final String result) {
		if(EchoCheckerFactory.alwaysReturnFalse) {
			return CheckResult.newError("checker was set to always return false");
		}
		final String expected = instance.toString().isEmpty() ? "" : instance.toString()+"\n";
		if(expected.toString().equals(result)) {
			return CheckResult.SUCCESS;
		}
		return CheckResult.newError("different strings (got \""+result+"\" instead of \""+expected+"\")");
	}
	
	private class EchoExecutor extends ASoftwareExecutor<EchoInstance> {
		
		public EchoExecutor(final Path execPath) {
			super(execPath);
		}

		@Override
		protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final EchoInstance instance) {
			return Stream.of(execLocation.toAbsolutePath().toString(), instanceFiles.get(".txt").toAbsolutePath().toString()).collect(Collectors.toList());
		}
		
	}
	
	public static void setAlwaysReturnFalse(final boolean value) {
		EchoCheckerFactory.alwaysReturnFalse = value;
	}

	@Override
	public List<MethodOption> getOptions() {
		return Collections.singletonList(new MethodOption("ignAll", "ignore all instances", v -> this.ignoreAll = true));
	}

	@Override
	public boolean ignoreInstance(EchoInstance instance) {
		return this.ignoreAll;
	}

}
