package fr.cril.rubens.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * A class used to retrieve all the available instances of some classes implementing a given interface parameterized by annotations setting them an "enabled" flag and a unique name.
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
 */
public abstract class AReflector<F> {
	
	/** the logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(AReflector.class);
	
	/** the mapping from implementor names to the implementing classes */
	private final Map<String, Class<?>> classes = new HashMap<>();
	
	/** the mapping from family names to implementor names */
	private final LinkedHashMap<String, List<String>> families = new LinkedHashMap<>();
	
	/** the names of the implementors that do not belongs to a family */
	private final List<String> withoutFamily = new ArrayList<>();
	
	/** the implemented type */
	private final Class<F> interfaceClass;
	
	/**
	 * Builds a new reflector given the types of both interface and annotation under consideration.
	 * 
	 * @param interfaceClass the type of the interface under consideration
	 */
	protected AReflector(final Class<F> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	/**
	 * Adds a class that would not have been discovered by the reflection mechanism.
	 * 
	 * This class does not need to be annotated.
	 * It is considered to be set as enabled with the provided name.
	 * The same uniqueness restriction apply for the name.
	 * 
	 * The implementor has no family.
	 * 
	 * @param implementorName the implementor name
	 * @param classClass the class class
	 */
	public void addClass(final String implementorName, final Class<? extends F> classClass) {
		addClass(implementorName, "", classClass);
	}
	
	/**
	 * Adds a class that would not have been discovered by the reflection mechanism.
	 * 
	 * This class does not need to be annotated.
	 * It is considered to be set as enabled with the provided name.
	 * The same uniqueness restriction apply for the name.
	 * 
	 * The implementor family is given by the second parameter and cannot be <code>null</code>.
	 * An empty string indicates no family.
	 * 
	 * @param implementorName the implementor name
	 * @param family the family
	 * @param classClass the class class
	 */
	public void addClass(final String implementorName, final String family, final Class<? extends F> classClass) {
		if(this.classes.containsKey(implementorName)) {
			final IllegalStateException exception = new IllegalStateException(this.classes.get(implementorName).getCanonicalName()+" and "
					+ classClass.getCanonicalName()+" share the same name (given by the annotation "+ReflectorParam.class.getCanonicalName());
			LOGGER.error(exception.getMessage());
			throw exception;
		}
		this.classes.put(implementorName, classClass);
		if(family.isEmpty()) {
			this.withoutFamily.add(implementorName);
		} else {
			this.families.computeIfAbsent(family, k -> new ArrayList<>()).add(implementorName);
		}
	}
	
	/**
	 * Uses reflection to recompute the set of available implementors.
	 * 
	 * Classes added by {@link AReflector#addClass(String, Class)} are removed.
	 */
	public void resetClasses() {
		this.classes.clear();
		this.families.clear();
		this.withoutFamily.clear();
		final FastClasspathScanner scanner = new FastClasspathScanner("fr.cril.rubens");
		final Set<Class<?>> classClasses = new HashSet<>();
		scanner.matchClassesImplementing(this.interfaceClass, classClasses::add);
		scanner.scan();
		for(final Class<?> classClass0 : classClasses) {
			@SuppressWarnings({ "unchecked" })
			final Class<? extends F> fClass = (Class<? extends F>) classClass0;
			ReflectorParam annotation = fClass.getAnnotation(ReflectorParam.class);
			if(annotation == null) {
				final IllegalStateException exception = new IllegalStateException(fClass.getCanonicalName()+" has no "
						+ ReflectorParam.class.getCanonicalName()+" annotation");
				LOGGER.error(exception.getMessage());
				throw exception;
			}
			if(!annotation.enabled()) {
				continue;
			}
			addClass(annotation.name(), annotation.family(), fClass);
		}
	}
	
	/**
	 * Returns the names of the implementor families.
	 * 
	 * @return the names of the implementor families
	 */
	public Collection<String> familiesNames() {
		return Collections.unmodifiableSet(this.families.keySet());
	}
	
	/**
	 * Returns the names of the implementors that belongs to a given family.
	 * 
	 * If no such family is defined, an {@link IllegalArgumentException} is thrown.
	 * The set of family names may be obtained via {@link AReflector#familiesNames()}.
	 * 
	 * @param familyName the name of the family under consideration
	 * @return the names of the implementors that belongs to the family
	 */
	public Collection<String> family(final String familyName) {
		final List<String> family = this.families.get(familyName);
		if(family == null) {
			throw new IllegalArgumentException("no such family: "+familyName);
		}
		return Collections.unmodifiableCollection(family);
	}
	
	/**
	 * Returns the names of the implementors that do not belong to a family.
	 * 
	 * If no such implementors exist, an empty collection is returned.
	 * 
	 * @return the names of the implementors that do not belong to a family
	 */
	public Collection<String> withoutFamily() {
		return Collections.unmodifiableCollection(this.withoutFamily);
	}
	
	/**
	 * Returns the names of the implementors discovered by the reflection process.
	 * 
	 * @return the names of the implementors
	 */
	public Collection<String> classesNames() {
		return Collections.unmodifiableSet(this.classes.keySet());
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
			final IllegalArgumentException exception = new IllegalArgumentException("cannot instantiate class: "+name, e);
			LOGGER.error(exception.getMessage());
			throw exception;
		}
	}
	
}
