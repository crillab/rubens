package fr.cril.rubens.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * A class used to retrieve all the available instances of some classes implementing a given interface parameterized by some special annotations setting them an "enabled" flag and a unique name.
 * The classes under consideration must implement the default constructor.
 * 
 * It ignores the classes marked as disabled and allows to get a new instance of one given its name.
 * Each class must be disabled or named by a unique string.
 * 
 * As this class uses the reflection mechanism, classes must be correctly exported in order to be visible.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <F> the class type
 * @param <P> the parameterization annotation type
 */
public abstract class AReflector<F, P extends Annotation> {
	
	/** the logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(AReflector.class);
	
	/** the mapping from implementor names to the implementing classes */
	private final Map<String, Class<?>> classes = new HashMap<>();
	
	/** the implemented type */
	private final Class<F> interfaceClass;
	
	/** the parametrization annotation type */
	private final Class<P> paramClass;

	/**
	 * Builds a new reflector given the types of both interface and annotation under consideration.
	 * 
	 * @param interfaceClass the type of the interface under consideration
	 * @param paramClass the type of the annotation under consideration
	 */
	protected AReflector(final Class<F> interfaceClass, final Class<P> paramClass) {
		this.interfaceClass = interfaceClass;
		this.paramClass = paramClass;
	}
	
	/**
	 * Adds a class that would not have been discovered by the reflection mechanism.
	 * 
	 * This class does not need to be annotated.
	 * It is considered to be set as enabled with the provided name.
	 * The same uniqueness restriction apply for the name.
	 * 
	 * @param implementorName the implementor name
	 * @param classClass the class class
	 */
	public void addClass(final String implementorName, final Class<? extends F> classClass) {
		if(this.classes.containsKey(implementorName)) {
			final IllegalStateException exception = new IllegalStateException(this.classes.get(implementorName).getCanonicalName()+" and "
					+ classClass.getCanonicalName()+" share the same name (given by the annotation "+this.paramClass.getCanonicalName());
			LOGGER.error(exception.getMessage());
			throw exception;
		}
		this.classes.put(implementorName, classClass);
	}
	
	/**
	 * Uses reflection to recompute the set of available implementors.
	 * 
	 * Classes added by {@link AReflector#addClass(String, Class)} are removed.
	 */
	public void resetClasses() {
		this.classes.clear();
		final FastClasspathScanner scanner = new FastClasspathScanner("fr.cril.rubens");
		final Set<Class<?>> classClasses = new HashSet<>();
		scanner.matchClassesImplementing(this.interfaceClass, classClasses::add);
		scanner.scan();
		for(final Class<?> classClass0 : classClasses) {
			@SuppressWarnings({ "unchecked" })
			final Class<? extends F> fClass = (Class<? extends F>) classClass0;
			P annotation = fClass.getAnnotation(this.paramClass);
			if(annotation == null) {
				final IllegalStateException exception = new IllegalStateException(fClass.getCanonicalName()+" has no "
						+ this.paramClass.getCanonicalName()+" annotation");
				LOGGER.error(exception.getMessage());
				throw exception;
			}
			if(!isEnabled(annotation)) {
				continue;
			}
			final String className = getName(annotation);
			addClass(className, fClass);
		}
	}
	
	/**
	 * Returns the value of the "enable" flag contained in the annotation.
	 * 
	 * @param paramAnnotation the annotation
	 * @return the value of the "enable" flag contained in the annotation
	 */
	protected abstract boolean isEnabled(P paramAnnotation);
	
	/**
	 * Returns the name defined in the annotation.
	 * 
	 * @param paramAnnotation the annotation
	 * @return the name defined in the annotation
	 */
	protected abstract String getName(P paramAnnotation);
	
	/**
	 * Returns the names of the implementors discovered by the reflection process.
	 * 
	 * @return the names of the implementors
	 */
	public Collection<String> classesNames() {
		return this.classes.keySet();
	}
	
	/**
	 * Returns the class associated with the provided name.
	 * 
	 * The name must refer to an existing implementor; the list of available ones may be got calling {@link AReflector#classesNames()}.
	 * The selected class must provided an unparameterized constructor. 
	 * 
	 * @param name the name of the implementor
	 * @return a new instance of the corresponding class
	 * @throws IllegalArgumentException if the provided name does not correspond to an implementor, or an error occurred while instantiating it
	 */
	@SuppressWarnings("unchecked")
	public F getClassInstance(final String name) {
		final Class<? extends F> clazz = (Class<? extends F>) this.classes.get(name);
		if(clazz == null) {
			final IllegalArgumentException exception = new IllegalArgumentException("\""+name+"\": no such class");
			LOGGER.error(exception.getMessage());
			throw exception;
		}
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			final IllegalArgumentException exception = new IllegalArgumentException("cannot instantiate class: \"+name+\"", e);
			LOGGER.error(exception.getMessage());
			throw exception;
		}
	}
	
}
