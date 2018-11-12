package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * A {@link CheckerFactory} for the SE-SST problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="SE-SST")
public class SESSTChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public SESSTChecker() {
		super(SemistableSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-SST");
	}

}
