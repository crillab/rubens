package fr.cril.rubens.reflection;

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
	 * Returns a flag indicating the instance can be used.
	 * 
	 * @return a flag indicating the instance can be used
	 */
	boolean enabled() default true;

}
