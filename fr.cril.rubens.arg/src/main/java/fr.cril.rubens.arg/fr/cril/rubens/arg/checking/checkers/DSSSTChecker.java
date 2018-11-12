package fr.cril.rubens.arg.checking.checkers;

import fr.cril.rubens.arg.checking.AbstractCheckerFactory;
import fr.cril.rubens.arg.checking.SoftwareOutputChecker;
import fr.cril.rubens.arg.testgen.SemistableSemTestGeneratorFactory;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A {@link CheckerFactory} for the DS-SST problem.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@CheckerFactoryParams(name="DS-SST")
public class DSSSTChecker extends AbstractCheckerFactory {
	
	/**
	 * Builds a new instance of this factory.
	 */
	public DSSSTChecker() {
		super(() -> new SemistableSemTestGeneratorFactory(false), SoftwareOutputChecker.DS::check, "DS-SST");
	}

}
