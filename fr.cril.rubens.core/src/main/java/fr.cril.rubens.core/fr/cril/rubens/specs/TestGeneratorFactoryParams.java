package fr.cril.rubens.specs;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to parameterized instances of {@link TestGeneratorFactory}.
 * 
 * ALL {@link TestGeneratorFactory} implementors MUST by annotated by this class and meet at least one of the following requirements:
 * <ul>
 * <li> declare the implementor as disabled (see {@link TestGeneratorFactoryParams#enabled()};</li>
 * <li> give a unique name to the implementor (see {@link TestGeneratorFactoryParams#name()}).</li>
 * </ul>
 * 
 * The need of this annotation comes from the reflection algorithm used to discover all the accessible {@link TestGeneratorFactory} instances.
 * Allowing to disable such factories allows to ignore abstract factories, while giving a name helps at providing to the users a simple way to select a factory.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface TestGeneratorFactoryParams {
	
	/**
	 * Returns the name associated with the {@link TestGeneratorFactory} instance.
	 * 
	 * @return the name associated with the instance
	 */
	String name() default "";
	
	/**
	 * Returns a flag indicating the {@link TestGeneratorFactory} instance can be used.
	 * 
	 * @return a flag indicating the {@link TestGeneratorFactory} instance can be used
	 */
	boolean enabled() default true;

}
