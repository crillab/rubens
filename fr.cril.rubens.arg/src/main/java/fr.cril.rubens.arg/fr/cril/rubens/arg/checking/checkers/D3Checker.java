package fr.cril.rubens.arg.checking.checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.cril.rubens.arg.checking.SoftwareExecutor;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.checking.SyntaxErrorException;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.D3TestGeneratorFactory;
import fr.cril.rubens.core.CheckResult;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A checker factory for the D3 ("Dung's Triathlon") track.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="D3")
public class D3Checker implements CheckerFactory<D3ArgumentationFramework> {
	
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
	public String execSoftware(final String exec, final D3ArgumentationFramework instance) {
		return SoftwareExecutor.execSoftware(exec, "D3", new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), ExtensionSet.getInstance(Collections.emptySet())));
	}

	@Override
	public CheckResult checkSoftwareOutput(final D3ArgumentationFramework instance, final String result) {
		List<String> extensionSets;
		try {
			extensionSets = splitExtensionSets(result);
		} catch (SyntaxErrorException e) {
			return CheckResult.newError(e.getMessage());
		}
		final CheckResult grCheck = SoftwareOutputChecker.EE.check(new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), instance.getGrExts()), extensionSets.get(0));
		if(!grCheck.isSuccessful()) {
			return CheckResult.newError("in GR part: "+grCheck.getExplanation());
		}
		final CheckResult stCheck = SoftwareOutputChecker.EE.check(new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), instance.getStExts()), extensionSets.get(1));
		if(!stCheck.isSuccessful()) {
			return CheckResult.newError("in ST part: "+stCheck.getExplanation());
		}
		final CheckResult prCheck = SoftwareOutputChecker.EE.check(new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), instance.getPrExts()), extensionSets.get(2));
		if(!prCheck.isSuccessful()) {
			return CheckResult.newError("in PR part: "+stCheck.getExplanation());
		}
		return CheckResult.SUCCESS;
	}

	/**
	 * Splits the results of the concatenated three subtracks and return them as an array.
	 * 
	 * An exception is raised if the number of subresults is not 3.
	 * 
	 * @param result the concatenated subtracks
	 * @return a list of the subresults
	 * @throws SyntaxErrorException if the number of subresults is not 3
	 */
	private List<String> splitExtensionSets(final String result) throws SyntaxErrorException {
		int brackets = 0;
		final char[] chars = result.replaceAll("\\s", "").toCharArray();
		StringBuilder current = new StringBuilder();
		final List<String> extSets = new ArrayList<>();
		for(int i=0; i<chars.length; ++i) {
			final char c = chars[i];
			if(c == '[') {
				current.append(c);
				brackets++;
			} else if(c==',' && brackets==0) {
				extSets.add(current.toString());
				current = new StringBuilder();
			} else if(brackets == 0) {
				throw new SyntaxErrorException("syntax error: \""+result+"\" is not a valid result");
			} else {
				current.append(c);
				if(c == ']') {
					brackets--;
				}
			}
		}
		extSets.add(current.toString());
		if(extSets.size() != 3) {
			throw new SyntaxErrorException("syntax error: \""+result+"\" is not a valid result");
		}
		return extSets;
	}

}
