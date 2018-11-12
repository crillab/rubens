package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.PreferredSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.ReflectorParam;

/**
 * A {@link CheckerFactory} for the DC-PR problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="DC-PR")
public class DCPRChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DCPRChecker() {
		super(() -> new PreferredSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-PR");
	}

}
