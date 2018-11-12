package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.IdealSemTestGeneratorFactory;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * A {@link CheckerFactory} for the SE-ID problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="SE-ID")
public class SEIDChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public SEIDChecker() {
		super(IdealSemTestGeneratorFactory::new, SoftwareOutputChecker.SE::check, "SE-ID");
	}

}
