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

import java.nio.file.Path;
import java.util.List;

import fr.cril.rubens.arg.checking.AFSolverExecutor;
import fr.cril.rubens.arg.checking.decoders.ISolverOutputDecoder;
import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;
import fr.cril.rubens.arg.checking.decoders.SyntaxErrorException;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkCheckerFactory;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.arg.testgen.DynamicSemTestGeneratorFactoryDecorator;
import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.arg.utils.CommonOptions;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.options.MethodOption;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.utils.ASoftwareExecutor;

/**
 * A decorator for {@link ElementaryCheckers} checkers that allows to check dynamic AFs.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(enabled=false)
public abstract class ADynamicElementaryCheckerFactoryDecorator implements ArgumentationFrameworkCheckerFactory<DynamicArgumentationFramework> {

	private final ArgumentationFrameworkCheckerFactory<ArgumentationFramework> decorated;
	
	private final EExtensionSetComputer extensionSetComputer;
	
	private ISolverOutputDecoder outputFormatDecoder = SolverOutputDecoderFactory.getDefault().getDecoderInstance();

	private String problem;
	
	/**
	 * Builds an instance of this class given the decorated checker, the extension set computer used for the problem, and the textual representation of the problem.
	 * 
	 * @param decorated the decorated checker
	 * @param extensionSetComputer the extension set computer
	 * @param problem the textual representation of the problem
	 */
	public ADynamicElementaryCheckerFactoryDecorator(final ArgumentationFrameworkCheckerFactory<ArgumentationFramework> decorated, final EExtensionSetComputer extensionSetComputer, final String problem) {
		this.decorated = decorated;
		this.extensionSetComputer = extensionSetComputer;
		this.problem = problem;
	}

	@Override
	public TestGeneratorFactory<DynamicArgumentationFramework> newTestGenerator() {
		return new DynamicSemTestGeneratorFactoryDecorator(this.decorated.newTestGenerator(), this.extensionSetComputer);
	}
	
	@Override
	public ASoftwareExecutor<DynamicArgumentationFramework> newExecutor(final Path execPath) {
		return new AFSolverExecutor<>(execPath, this.problem);
	}

	@Override
	public CheckResult checkSoftwareOutput(final DynamicArgumentationFramework instance, final String result) {
		final List<String> results;
		try {
			results = splitResults(result);
		} catch(SyntaxErrorException e) {
			return CheckResult.newError(e.getMessage());
		}
		if(results.size() != instance.getTranslations().size()+1) {
			return CheckResult.newError("wrong number of results (got "+result+" for an instance with "+instance.getTranslations().size()+" dynamic attacks)");
		}
		final CheckResult initResult = this.decorated.checkSoftwareOutput(instance.getInitInstance(), results.get(0));
		if(!initResult.isSuccessful()) {
			return CheckResult.newError("in initial instance: "+initResult.getExplanation());
		}
		for(int i=0; i<instance.getTranslations().size(); ++i) {
			final CheckResult checkResult = this.decorated.checkSoftwareOutput(instance.getTranslations().get(i).getArgumentationFramework(), results.get(i+1));
			if(!checkResult.isSuccessful()) {
				return CheckResult.newError("after "+(i+1)+" changes: "+checkResult.getExplanation());
			}
		}
		return CheckResult.SUCCESS;
	}

	private List<String> splitResults(final String result) throws SyntaxErrorException {
		final String task = this.problem.substring(0, 2);
		if("DS".equals(task) || "DC".equals(task)) {
			return this.outputFormatDecoder.splitDynAcceptanceStatuses(result);
		} else if("SE".equals(task)) {
			return this.outputFormatDecoder.splitDynExtensions(result);
		} else if("EE".equals(task)) {
			return this.outputFormatDecoder.splitDynExtensionSets(result);
		}
		throw new IllegalArgumentException();
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
