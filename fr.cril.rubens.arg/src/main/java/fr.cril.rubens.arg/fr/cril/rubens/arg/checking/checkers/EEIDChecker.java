package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.IdealSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the EE-ID problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="EE-ID")
public class EEIDChecker extends AbstractCheckerFactory {

	/**
	 * Builds a new instance of this factory.
	 */
	public EEIDChecker() {
		super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-ID");
	}

}
