package fr.cril.rubens.cnf.mc;

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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.cril.rubens.cnf.core.ASatCheckerFactory;
import fr.cril.rubens.cnf.core.CnfSolverExecutor;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A checker factory dedicated to model counters.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="sharpSAT", family="CNF/SAT")
public class ModelCounterCheckerFactory extends ASatCheckerFactory<ModelCountingCnfInstance> {
	
	/**
	 * Builds a new factory instance.
	 */
	public ModelCounterCheckerFactory() {
		super();
	}

	@Override
	public TestGeneratorFactory<ModelCountingCnfInstance> newTestGenerator() {
		return new ModelCountingCnfTestGeneratorFactory();
	}

	@Override
	public ASoftwareExecutor<ModelCountingCnfInstance> newExecutor(final Path execPath) {
		return new CnfSolverExecutor<>(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final ModelCountingCnfInstance instance, final String result) {
		String status = null;
		final List<String> lines = Arrays.stream(result.split("\n")).collect(Collectors.toList());
		for(final String line : lines) {
			if(line.startsWith("c")) {
				continue;
			}
			if(line.startsWith("s")) {
				if(status != null) {
					return CheckResult.newError("multiple status line");
				}
				status = line;
			} else {
				return CheckResult.newError("unexpected line: \""+line+"\"");
			}
		}
		if(status == null) {
			return CheckResult.newError("no status line");
		}
		if(status.length() < 2 || status.charAt(1) != ' ') {
			return CheckResult.newError("wrong status line: \""+status+"\"");
		}
		final String strCount = status.substring(2);
		try {
			int count = Integer.parseInt(strCount);
			return count == instance.models().size() ? CheckResult.SUCCESS : CheckResult.newError("wrong model count (got "+count+", expected "+instance.models().size()+")");
		} catch(NumberFormatException e) {
			return CheckResult.newError("wrong number of models provided: \""+strCount+"\"");
		}
	}

}
