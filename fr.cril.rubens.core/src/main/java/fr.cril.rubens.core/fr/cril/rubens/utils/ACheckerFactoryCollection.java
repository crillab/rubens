package fr.cril.rubens.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.cril.rubens.reflection.CheckerFactoryReflector;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.CheckerFactory;
import fr.cril.rubens.specs.CheckerFactoryCollection;
import fr.cril.rubens.specs.Instance;

/**
 * Common code for {@link CheckerFactoryCollection} instances.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under consideration
 */
@ReflectorParam(enabled=false)
public abstract class ACheckerFactoryCollection<T extends Instance> implements CheckerFactoryCollection<T> {
	
	private final Map<String, CheckerFactory<T>> factories;
	
	/**
	 * Builds a factory collection given the factory names.
	 * 
	 * @param factoryNames the factory names
	 */
	protected ACheckerFactoryCollection(final List<String> factoryNames) {
		final CheckerFactoryReflector reflector = CheckerFactoryReflector.getInstance();
		final Collection<String> availableFactories = reflector.classesNames();
		final String unexpected = factoryNames.stream().filter(n -> !availableFactories.contains(n)).reduce((a,b) -> a+", "+b).orElse("");
		if(!unexpected.equals("")) {
			throw new IllegalArgumentException("unavailable factories: "+unexpected);
		}
		this.factories = factoryNames.stream().collect(Collectors.toMap(k -> k, reflector::getClassInstance));
	}
	
	@Override
	public final Set<String> getNames() {
		return this.factories.keySet();
	}
	
	@Override
	public final CheckerFactory<T> getFactory(final String name) {
		return this.factories.get(name);
	}

}
