package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.StableSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the EE-ST problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="EE-ST")
public class EESTChecker extends AbstractCheckerFactory {

	/**
	 * Builds a new instance of this factory.
	 */
	public EESTChecker() {
		super(StableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ST");
	}

}
