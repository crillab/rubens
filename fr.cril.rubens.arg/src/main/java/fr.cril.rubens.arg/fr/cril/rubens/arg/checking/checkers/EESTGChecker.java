package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.StageSemTestGeneratorFactory;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * A {@link CheckerFactory} for the EE-STG problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="EE-STG")
public class EESTGChecker extends AbstractCheckerFactory {

	/**
	 * Builds a new instance of this factory.
	 */
	public EESTGChecker() {
		super(StageSemTestGeneratorFactory::new, SoftwareOutputChecker.EE::check, "EE-STG");
	}

}
