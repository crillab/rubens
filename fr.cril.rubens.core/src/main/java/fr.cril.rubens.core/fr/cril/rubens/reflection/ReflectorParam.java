package fr.cril.rubens.reflection;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to parameterized instances of interfaces involved in reflection processes.
 * 
 * ALL implementors MUST by annotated by this class and meet at least one of the following requirements:
 * <ul>
 * <li> declare the implementor as disabled: in this case, it will not be retrieved by the reflection process;</li>
 * <li> give a unique name to the implementor (the uniqueness is restricted to the interface under reflection).</li>
 * </ul>
 * 
 * The need of this annotation comes from the reflection algorithm used to discover all the accessible instances.
 * Allowing to disable such factories is used to ignore abstract factories, while giving a name helps at providing the users a simple way to select a factory.
 * 
 * Providing a family name is optional. It can help for some processes, like sorting parameterized classes before printing them.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface ReflectorParam {
	
	/**
	 * Returns the name associated with the instance.
	 * 
	 * @return the name associated with the instance
	 */
	String name() default "";
	
	/**
	 * Returns the name of the family the parameterized class belongs to.
	 * 
	 * @return the name of the family the parameterized class belongs to
	 */
	String family() default "";
	
	/**
	 * Returns a flag indicating the instance can be used.
	 * 
	 * @return a flag indicating the instance can be used
	 */
	boolean enabled() default true;

}
