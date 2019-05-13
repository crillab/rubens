package fr.cril.rubens.utils;

/*-
 * #%L
 * RUBENS core API
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private final Map<String, CheckerFactory<T>> factories = new LinkedHashMap<>();
	
	/**
	 * Builds a factory collection given the factory names.
	 * 
	 * @param factoryNames the factory names
	 */
	@SuppressWarnings("unchecked")
	protected ACheckerFactoryCollection(final List<String> factoryNames) {
		final CheckerFactoryReflector reflector = CheckerFactoryReflector.getInstance();
		final Collection<String> availableFactories = reflector.classesNames();
		final String unexpected = factoryNames.stream().filter(n -> !availableFactories.contains(n)).reduce((a,b) -> a+", "+b).orElse("");
		if(!unexpected.equals("")) {
			throw new IllegalArgumentException("unavailable factories: "+unexpected);
		}
		factoryNames.stream().forEach(k -> this.factories.put(k, reflector.getClassInstance(k)));
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
