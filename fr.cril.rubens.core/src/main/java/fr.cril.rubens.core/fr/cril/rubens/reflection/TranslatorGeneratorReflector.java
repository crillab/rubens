package fr.cril.rubens.reflection;

import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A class used to retrieve all the available {@link TestGeneratorFactory} instances.
 * It implements the Singleton design pattern; call {@link TranslatorGeneratorReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link ReflectorParam} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link TestGeneratorFactory} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@SuppressWarnings("rawtypes")
public class TranslatorGeneratorReflector extends AReflector<TestGeneratorFactory> {
	
	/** the instance of {@link TranslatorGeneratorReflector} */
	private static TranslatorGeneratorReflector instance = null;
	
	/**
	 * Returns the (only) instance of the reflector.
	 * @return the instance of the reflector
	 */
	public static TranslatorGeneratorReflector getInstance() {
		if(instance == null) {
			instance = new TranslatorGeneratorReflector();
		}
		return instance;
	}

	private TranslatorGeneratorReflector() {
		super(TestGeneratorFactory.class);
		resetClasses();
	}

}
