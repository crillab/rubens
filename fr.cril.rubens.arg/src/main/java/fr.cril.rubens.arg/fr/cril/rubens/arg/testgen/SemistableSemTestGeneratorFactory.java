package fr.cril.rubens.arg.testgen;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A {@link TestGeneratorFactory} instance used to build abstract argumentation problems for the Semistable semantics.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="ARG-SST")
public class SemistableSemTestGeneratorFactory extends ASemTestGeneratorFactory {

	/**
	 * Builds a new factory for this semantics with the empty instance used as the root test generation instance.
	 */
	public SemistableSemTestGeneratorFactory() {
		this(true);
	}

	/**
	 * Builds a new factory for this semantics.
	 * 
	 * A flag is used to allow/disallow the empty argumentation framework.
	 * In case it is allowed, it is the root instance.
	 * In the other case, the root instance is the one having a single argument and no attacks.
	 * 
	 * @param emptyInstanceAllowed the flag used to allow/disallow the empty argumentation framework
	 */
	public SemistableSemTestGeneratorFactory(final boolean emptyInstanceAllowed) {
		super(EExtensionSetComputer.SEMISTABLE_SEM, emptyInstanceAllowed);
	}

}
