package fr.cril.rubens.checker.utils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.Option;
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
	public List<Option> getOptions() {
		return Collections.singletonList(new Option("ignAll", "ignore all instances", v -> this.ignoreAll = true));
	}

	@Override
	public boolean ignoreInstance(EchoInstance instance) {
		return this.ignoreAll;
	}

}
