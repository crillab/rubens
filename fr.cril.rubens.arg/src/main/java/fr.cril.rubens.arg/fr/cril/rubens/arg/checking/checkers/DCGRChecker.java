package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.GroundedSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the DC-GR problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="DC-GR")
public class DCGRChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DCGRChecker() {
		super(() -> new GroundedSemTestGeneratorFactory(false), SoftwareOutputChecker.DC::check, "DC-GR");
	}

}
