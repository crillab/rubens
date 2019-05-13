package fr.cril.rubens.arg.checking.checkers;

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

import java.nio.file.Path;
import java.util.List;

import fr.cril.rubens.arg.checking.AFSolverExecutor;
import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.D3TestGeneratorFactory;
import fr.cril.rubens.arg.utils.CommonOptions;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A checker factory for the D3 ("Dung's Triathlon") track.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="D3", family="AF/Triathlon")
public class D3Checker implements ArgumentationFrameworkCheckerFactory<D3ArgumentationFramework> {
	
	/** the decoder used to read the solver output */ 
	private ISolverOutputDecoder outputFormatDecoder = SolverOutputDecoderFactory.getDefault().getDecoderInstance();
	
	/**
	 * Builds a new instance of this factory.
	 */
	public D3Checker() {
		// nothing to do here
	}

	@Override
	public TestGeneratorFactory<D3ArgumentationFramework> newTestGenerator() {
		return new D3TestGeneratorFactory();
	}

	@Override
	public ASoftwareExecutor<D3ArgumentationFramework> newExecutor(final Path execPath) {
		return new AFSolverExecutor<>(execPath, "D3");
	}

	@Override
	public CheckResult checkSoftwareOutput(final D3ArgumentationFramework instance, final String result) {
		try {
			final List<ExtensionSet> extSets = this.outputFormatDecoder.readD3(result);
			if(!instance.getGrExts().equals(extSets.get(0))) {
				return newPartialError("GR", extSets.get(0), instance.getGrExts());
			}
			if(!instance.getStExts().equals(extSets.get(1))) {
				return newPartialError("ST", extSets.get(1), instance.getStExts());
			}
			if(!instance.getPrExts().equals(extSets.get(2))) {
				return newPartialError("PR", extSets.get(2), instance.getPrExts());
			}
		} catch (SyntaxErrorException e) {
			return CheckResult.newError(e.getMessage());
		}
		return CheckResult.SUCCESS;
	}
	
	private CheckResult newPartialError(final String part, final ExtensionSet got, final ExtensionSet expected) {
		return CheckResult.newError("in "+part+" part: got "+got+", expected "+expected);
	}

	@Override
	public void setOutputFormat(final ISolverOutputDecoder decoder) {
		this.outputFormatDecoder = decoder;
	}
	
	@Override
	public List<MethodOption> getOptions() {
		return CommonOptions.getInstance().getOptions(this);
	}

}
