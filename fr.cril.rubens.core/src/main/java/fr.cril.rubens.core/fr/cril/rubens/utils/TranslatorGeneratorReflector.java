package fr.cril.rubens.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.specs.Instance;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.specs.TestGeneratorFactoryParams;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * A class used to retrieve all the available {@link TestGeneratorFactory} instances.
 * It implements the Singleton design pattern; call {@link TranslatorGeneratorReflector#getInstance()} to get its (only) instance.
 * 
 * It ignores the factories marked as disabled and allows to get a new instance of one given its name.
 * Each factory may be disabled or named by a unique string.
 * See {@link TestGeneratorFactoryParams} annotation for more information about disabling and naming factories.
 * 
 * As this class uses the reflection mechanism, {@link TestGeneratorFactory} implementors must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class TranslatorGeneratorReflector {
	
	private static TranslatorGeneratorReflector instance = null;
	
	private final Map<String, Class<?>> factories = new HashMap<>();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranslatorGeneratorReflector.class);
	
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
		resetFactories();
	}

	/**
	 * Adds a factory that would not have been discovered by the reflection mechanism.
	 * 
	 * This factory does not need to be annotated.
	 * It is considered to be set as enabled with the provided name.
	 * The same unicity restriction apply for the name.
	 * 
	 * @param factoryName the factory name
	 * @param factoryClass the factory class
	 */
	public void addFactory(final String factoryName, @SuppressWarnings("rawtypes") final Class<? extends TestGeneratorFactory> factoryClass) {
		if(this.factories.containsKey(factoryName)) {
			final IllegalStateException exception = new IllegalStateException(this.factories.get(factoryName).getCanonicalName()+" and "
					+ factoryClass.getCanonicalName()+" share the same name (given by the annotation "+TestGeneratorFactoryParams.class.getCanonicalName());
			LOGGER.error(exception.getMessage());
			throw exception;
		}
		this.factories.put(factoryName, factoryClass);
	}
	
	/**
	 * Uses reflection to recompute the set of available factories.
	 * 
	 * Factories added by {@link TranslatorGeneratorReflector#addFactory(String, Class)} are removed.
	 */
	public void resetFactories() {
		this.factories.clear();
		final FastClasspathScanner scanner = new FastClasspathScanner("fr.cril.rubens");
		final Set<Class<?>> factoryClasses = new HashSet<>();
		scanner.matchClassesImplementing(TestGeneratorFactory.class, factoryClasses::add);
		scanner.scan();
		for(final Class<?> factoryClass0 : factoryClasses) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Class<? extends TestGeneratorFactory> factoryClass = (Class<? extends TestGeneratorFactory>) factoryClass0;
			TestGeneratorFactoryParams annotation = factoryClass.getAnnotation(TestGeneratorFactoryParams.class);
			if(annotation == null) {
				final IllegalStateException exception = new IllegalStateException(factoryClass.getCanonicalName()+" has no "
						+ TestGeneratorFactoryParams.class.getCanonicalName()+" annotation");
				LOGGER.error(exception.getMessage());
				throw exception;
			}
			if(!annotation.enabled()) {
				continue;
			}
			final String factoryName = annotation.name();
			addFactory(factoryName, factoryClass);
		}
	}
	
	/**
	 * Returns the names of the factories discovered by the reflection process.
	 * 
	 * @return the names of the factories
	 */
	public Collection<String> factoryNames() {
		return this.factories.keySet();
	}
	
	/**
	 * Returns the factory associated with the provided name.
	 * 
	 * The name must refer to an existing factory; the list of available ones may be got calling {@link TranslatorGeneratorReflector#factoryNames()}.
	 * The selected factory must provided an unparameterized constructor. 
	 * 
	 * @param name the name of the factory
	 * @return a new instance of the corresponding factory
	 * @throws IllegalArgumentException if the provided name does not correspond to a factory or an error occurred while instantiating it
	 */
	@SuppressWarnings("unchecked")
	public TestGeneratorFactory<Instance> getFactory(final String name) {
		final Class<? extends TestGeneratorFactory<?>> factory = (Class<? extends TestGeneratorFactory<?>>) this.factories.get(name);
		if(factory == null) {
			final IllegalArgumentException exception = new IllegalArgumentException("\""+name+"\": no such factory");
			LOGGER.error(exception.getMessage());
			throw exception;
		}
		try {
			return (TestGeneratorFactory<Instance>) factory.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			final IllegalArgumentException exception = new IllegalArgumentException("cannot instantiate factory: \"+name+\"", e);
			LOGGER.error(exception.getMessage());
			throw exception;
		}
	}
	
}
