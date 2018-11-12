package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.CompleteSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the DS-CO problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="DS-CO")
public class DSCOChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DSCOChecker() {
		super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-CO");
	}

}
