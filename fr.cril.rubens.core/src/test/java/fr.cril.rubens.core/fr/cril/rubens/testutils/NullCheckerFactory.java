package fr.cril.rubens.testutils;

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
import java.util.List;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

@ReflectorParam(enabled=false)
public class NullCheckerFactory implements CheckerFactory<Instance> {

	@Override
	public TestGeneratorFactory<Instance> newTestGenerator() {
		return null;
	}

	@Override
	public CheckResult checkSoftwareOutput(Instance instance, String result) {
		return null;
	}
	
	@Override
	public ASoftwareExecutor<Instance> newExecutor(Path execPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MethodOption> getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ignoreInstance(Instance instance) {
		// TODO Auto-generated method stub
		return false;
	}

}
