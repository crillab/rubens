package fr.cril.rubens.utils;

import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryParams;

/**
 * A class used to retrieve all the available {@link CheckerFactory} instances.
 * It implements the Singleton design pattern; call {@link CheckerFactoryReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link CheckerFactoryParams} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link CheckerFactory} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@SuppressWarnings("rawtypes")
public class CheckerFactoryReflector extends AReflector<CheckerFactory, CheckerFactoryParams>{
	
	/** the only instance of this reflector */
	private static CheckerFactoryReflector instance = null;

	/**
	 * Returns the (only) instance of the reflector.
	 * 
	 * @return the instance of the reflector
	 */
	public static CheckerFactoryReflector getInstance() {
		if(instance == null) {
			instance = new CheckerFactoryReflector();
		}
		return instance;
	}

	private CheckerFactoryReflector() {
		super(CheckerFactory.class, CheckerFactoryParams.class);
		resetClasses();
	}

	@Override
	protected boolean isEnabled(final CheckerFactoryParams paramAnnotation) {
		return paramAnnotation.enabled();
	}

	@Override
	protected String getName(final CheckerFactoryParams paramAnnotation) {
		return paramAnnotation.name();
	}

}
