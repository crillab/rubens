package fr.cril.rubens.cnf.core;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.core.CheckResult.ResultIsErrorException;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A checker factory dedicated to SAT solvers.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="SAT", family="CNF/SAT")
public class SatSolverCheckerFactory extends ASatCheckerFactory<CnfInstance> {
	
	@Override
	public TestGeneratorFactory<CnfInstance> newTestGenerator() {
		return new CnfTestGeneratorFactory();
	}

	@Override
	public ASoftwareExecutor<CnfInstance> newExecutor(final Path execPath) {
		return new CnfSolverExecutor<>(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final CnfInstance instance, final String result) {
		String status = null;
		final List<String> values = new ArrayList<>();
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
			} else if(line.startsWith("v")) {
				if(status == null) {
					return CheckResult.newError("a values line is present although no status line were written");
				}
				values.add(line);
			} else {
				return CheckResult.newError("unexpected line: \""+line+"\"");
			}
		}
		if(status == null) {
			return CheckResult.newError("no status line");
		}
		switch(status) {
		case "s SATISFIABLE":
			return checkSatResult(instance, values);
		case "s UNSATISFIABLE":
			return checkUnsatResult(instance, values);
		default:
			return CheckResult.newError("unexpected status line: \""+status+"\"");
		}
	}

	private CheckResult checkSatResult(final CnfInstance instance, final List<String> values) {
		if(values.isEmpty()) {
			return CheckResult.newError("status is SATISFIABLE but no values lines are provided");
		}
		final int[] arrayModel = IntStream.range(1, instance.nVars()+1).toArray();
		boolean lastZero = false;
		for(final String value : values) {
			try {
				lastZero = processValueLine(arrayModel, value, lastZero);
			} catch (ResultIsErrorException e) {
				return e.getErrorResult();
			}
		}
		if(!lastZero) {
			return CheckResult.newError("no final 0 in values");
		}
		final Set<Integer> model = Arrays.stream(arrayModel).boxed().collect(Collectors.toSet());
		return instance.models().contains(model) ? CheckResult.SUCCESS : CheckResult.newError("wrong model provided: "+model);
	}

	private boolean processValueLine(final int[] arrayModel, final String value, boolean lastZero) throws ResultIsErrorException {
		if(value.length() < 2 || value.charAt(1) != ' ') {
			throw CheckResult.newError("\"v\" is not followed by a space in a value line").asException();
		}
		for(final String word: value.substring(2).replaceAll("[ \t]+", " ").split(" ")) {
			if(lastZero) {
				throw CheckResult.newError("a value follows a 0 in a value line (0 was present in this line or in a preceeding one)").asException();
			}
			try {
				final int v = Integer.parseInt(word);
				if(v == 0) {
					lastZero = true;
				} else {
					arrayModel[Math.abs(v)-1] = v;
				}
			} catch(NumberFormatException e) {
				throw CheckResult.newError("wrong literal provided: \""+word+"\"").asException();
			}
		}
		return lastZero;
	}
	
	private CheckResult checkUnsatResult(final CnfInstance instance, final List<String> values) {
		if(!values.isEmpty()) {
			return CheckResult.newError("unexpected values line (status is UNSAT");
		}
		return instance.models().isEmpty() ? CheckResult.SUCCESS : CheckResult.newError("solver answered UNSATISFIABLE although formula has models");
	}

}
