package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the EE-SST problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="EE-SST")
public class EESSTChecker extends AbstractCheckerFactory {

	/**
	 * Builds a new instance of this factory.
	 */
	public EESSTChecker() {
		super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-SST");
	}

}
