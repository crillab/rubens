package fr.cril.rubens.arg.core;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
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

import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

public class ArgumentationFrameworkCheckerFactoryTest {
	
	@Test
	public void testIgnFilters() {
		assertFalse(new LocalFactory().ignoreInstance(new ArgumentationFramework()));
	}
	
	@ReflectorParam(enabled=false)
	private class LocalFactory implements ArgumentationFrameworkCheckerFactory<AArgumentationFrameworkGraph> {

		@Override
		public TestGeneratorFactory<AArgumentationFrameworkGraph> newTestGenerator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ASoftwareExecutor<AArgumentationFrameworkGraph> newExecutor(Path execPath) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CheckResult checkSoftwareOutput(AArgumentationFrameworkGraph instance, String result) {
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
			// TODO Auto-generated method stub
			
		}
		
	}

}
