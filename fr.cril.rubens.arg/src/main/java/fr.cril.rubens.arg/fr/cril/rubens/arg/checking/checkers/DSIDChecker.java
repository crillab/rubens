package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.IdealSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the DS-ID problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="DS-ID")
public class DSIDChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DSIDChecker() {
		super(() -> new IdealSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-ID");
	}

}
