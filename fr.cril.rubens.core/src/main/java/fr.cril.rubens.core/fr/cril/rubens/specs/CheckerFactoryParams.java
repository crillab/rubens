package fr.cril.rubens.specs;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to parameterized instances of {@link CheckerFactory}.
 * 
 * ALL {@link CheckerFactory} implementors MUST by annotated by this class and meet at least one of the following requirements:
 * <ul>
 * <li> declare the implementor as disabled (see {@link CheckerFactory#enabled()};</li>
 * <li> give a unique name to the implementor (see {@link CheckerFactory#name()}).</li>
 * </ul>
 * 
 * The need of this annotation comes from the reflection algorithm used to discover all the accessible {@link CheckerFactory} instances.
 * Allowing to disable such factories allows to ignore abstract factories, while giving a name helps at providing the users a simple way to select a factory.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface CheckerFactoryParams {
	
	/**
	 * Returns the name associated with the {@link CheckerFactory} instance.
	 * 
	 * @return the name associated with the instance
	 */
	String name() default "";
	
	/**
	 * Returns a flag indicating the {@link CheckerFactory} instance can be used.
	 * 
	 * @return a flag indicating the {@link CheckerFactory} instance can be used
	 */
	boolean enabled() default true;

}
