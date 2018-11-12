package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.CompleteSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the DC-CO problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="DC-CO")
public class DCCOChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DCCOChecker() {
		super(() -> new CompleteSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-CO");
	}

}
