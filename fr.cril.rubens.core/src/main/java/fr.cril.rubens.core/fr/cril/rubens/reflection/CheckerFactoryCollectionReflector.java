package fr.cril.rubens.reflection;

import fr.cril.rubens.specs.CheckerFactoryCollection;

/**
 * A class used to retrieve all the available {@link CheckerFactoryCollection} instances.
 * It implements the Singleton design pattern; call {@link CheckerFactoryCollectionReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link ReflectorParam} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link CheckerFactoryCollection} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@SuppressWarnings("rawtypes")
public class CheckerFactoryCollectionReflector extends AReflector<CheckerFactoryCollection> {
	
	/** the only instance of this reflector */
	private static CheckerFactoryCollectionReflector instance = null;

	/**
	 * Returns the (only) instance of the reflector.
	 * 
	 * @return the instance of the reflector
	 */
	public static CheckerFactoryCollectionReflector getInstance() {
		if(instance == null) {
			instance = new CheckerFactoryCollectionReflector();
		}
		return instance;
	}

	private CheckerFactoryCollectionReflector() {
		super(CheckerFactoryCollection.class);
		resetClasses();
	}

}
