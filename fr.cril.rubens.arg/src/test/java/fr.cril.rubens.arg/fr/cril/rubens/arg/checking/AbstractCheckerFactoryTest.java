package fr.cril.rubens.arg.checking;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;

public class AbstractCheckerFactoryTest {
	
	private AbstractCheckerFactory factory;
	
	private final Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier = SemistableSemTestGeneratorFactory::new;
	
	private final BiFunction<ArgumentationFramework, String, CheckResult> resultChecker = SoftwareOutputChecker.EE::check;
	
	@Before
	public void setUp() {
		Forget.all();
		this.factory = new Factory();
	}
	
	@Test
	public void testNewGenerator() {
		assertEquals(SemistableSemTestGeneratorFactory.class, this.factory.newTestGenerator().getClass());
	}
	
	@Test
	public void testCheckSoftwareOutput() {
		final Argument arg = Argument.getInstance("a");
		final ArgumentSet argSet = ArgumentSet.getInstance(Collections.singleton(arg));
		final ArgumentationFramework af = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.singleton(argSet)));
		final CheckResult result = this.factory.checkSoftwareOutput(af, "[[a]]");
		assertEquals(CheckResult.SUCCESS, result);
	}
	
	@ReflectorParam(enabled=false)
	private class Factory extends AbstractCheckerFactory {

		private Factory() {
			super(generatorSupplier, resultChecker, "EE-SST");
		}
		
	}

}
