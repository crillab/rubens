package fr.cril.rubens.arg.checking.checkers;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.checking.AFSolverExecutor;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.core.TriFunction;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.arg.utils.Forget;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;

class AbstractElementaryCheckerFactoryTest {
	
	private AbstractElementaryCheckerFactory factory;
	
	private final Supplier<TestGeneratorFactory<ArgumentationFramework>> generatorSupplier = SemistableSemTestGeneratorFactory::new;
	
	private final TriFunction<ArgumentationFramework, String, ISolverOutputDecoder, CheckResult> resultChecker = SoftwareOutputChecker.EE::check;
	
	@BeforeEach
	public void setUp() {
		Forget.all();
		this.factory = new Factory();
		this.factory.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
	}
	
	@Test
	void testNewGenerator() {
		assertEquals(SemistableSemTestGeneratorFactory.class, this.factory.newTestGenerator().getClass());
	}
	
	@Test
	void testCheckSoftwareOutput() {
		final Argument arg = Argument.getInstance("a");
		final ArgumentSet argSet = ArgumentSet.getInstance(Collections.singleton(arg));
		final ArgumentationFramework af = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.singleton(argSet)));
		final CheckResult result = this.factory.checkSoftwareOutput(af, "[[a]]");
		assertEquals(CheckResult.SUCCESS, result);
	}
	
	@Test
	void testNewExecutor() {
		assertTrue(this.factory.newExecutor(Paths.get("/foo/bar")) instanceof AFSolverExecutor);
	}
	
	@Test
	void testOptions() {
		assertEquals(1, this.factory.getOptions().size());
	}
	
	@ReflectorParam(enabled=false)
	private class Factory extends AbstractElementaryCheckerFactory {

		private Factory() {
			super(generatorSupplier, resultChecker, "EE-SST");
		}
		
	}

}
