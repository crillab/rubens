package fr.cril.rubens.specs;

import java.util.Set;

import fr.cril.rubens.reflection.ReflectorParam;

/**
 * An interface for collections of {@link CheckerFactory} instances.
 * 
 * This kind of collections allows for example to name series of checking processes.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under test
 */
public interface CheckerFactoryCollection<T extends Instance> {
	
	/**
	 * Returns the names of factories contained in this collection.
	 * 
	 * The names are the ones defined using the {@link ReflectorParam} annotation.
	 * 
	 * @return the names of factories contained in this collection
	 */
	Set<String> getNames();
	
	/**
	 * Returns the factory associated with the provided name.
	 * 
	 * The names of the factories are the ones defined using the {@link ReflectorParam} annotation.
	 * Call {@link CheckerFactoryCollection#getNames()} to get the available factories.
	 * 
	 * @param name the name of the factory
	 * @return the corresponding factory
	 */
	CheckerFactory<T> getFactory(final String name);

}
