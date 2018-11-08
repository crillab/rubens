package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.PreferredSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the EE-PR problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="EE-PR")
public class EEPRChecker extends AbstractCheckerFactory {

	/**
	 * Builds a new instance of this factory.
	 */
	public EEPRChecker() {
		super(PreferredSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-PR");
	}

}
