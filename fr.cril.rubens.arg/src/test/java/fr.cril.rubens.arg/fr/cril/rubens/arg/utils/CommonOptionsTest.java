package fr.cril.rubens.arg.utils;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class CommonOptionsTest {
	
	@Test
	public void testSingleton() {
		assertTrue(CommonOptions.getInstance() == CommonOptions.getInstance());
	}
	
	@Test
	public void testNames() {
		final List<String> actual = CommonOptions.getInstance().getOptions(new LocalFactory()).stream().map(MethodOption::getName).collect(Collectors.toList());
		final List<String> expected = Stream.of("outputFormat").collect(Collectors.toList());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testOutputFormat() {
		final LocalFactory factory = new LocalFactory();
		factory.setOutputFormat(SolverOutputDecoderFactory.ICCMA17.getDecoderInstance());
		final Map<String, MethodOption> opts = CommonOptions.getInstance().getOptions(factory).stream().collect(Collectors.toMap(MethodOption::getName, o -> o));
		opts.get("outputFormat").apply("ICCMA19");
		assertFalse(factory.isOutputFormatNull());
	}
	
	@ReflectorParam(enabled=false)
	private class LocalFactory implements ArgumentationFrameworkCheckerFactory<ArgumentationFramework> {
		
		private ISolverOutputDecoder outputFormat;

		@Override
		public TestGeneratorFactory<ArgumentationFramework> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<ArgumentationFramework> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(ArgumentationFramework instance, String result) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<MethodOption> getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setOutputFormat(ISolverOutputDecoder decoder) {
			this.outputFormat = decoder;
		}
		
		private boolean isOutputFormatNull() {
			return this.outputFormat == null;
		}
		
	}

}
