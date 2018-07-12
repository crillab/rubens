package fr.cril.rubens.specs;

import java.util.List;

/**
 * An interface for factories providing all the element needed for a generation process.
 * <ul>
 * <li> {@link TestGeneratorFactory#initInstance()} provides the root instance; </li>
 * <li> {@link TestGeneratorFactory#translators()} provides the list of translators used to generate the instances. </li>
 * </ul>
 * 
 * A {@link TestGeneratorFactory} implementor MUST be parameterized by a {@link TestGeneratorFactoryParams} annotation.
 * The annotation declaration MUST meet at least one of the following requirements:
 * <ul>
 * <li> declare the implementor as disabled (see {@link TestGeneratorFactoryParams#enabled()};</li>
 * <li> give a unique name to the implementor (see {@link TestGeneratorFactoryParams#name()}).</li>
 * </ul>
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances under consideration
 */
public interface TestGeneratorFactory<T extends Instance> {
	
	/**
	 * Returns the instance to use as the entry point of the generation algorithm.
	 * 
	 * It is usually a tautological instance.
	 * 
	 * @return the root instance
	 */
	T initInstance();
	
	/**
	 * Returns the list of translators to use in the generation process.
	 * 
	 * A translator may occur multiple times: in this case, it should be applied as times as returned here.
	 * This makes sense in case a generator has a random part in its algorithm.
	 * 
	 * @return the list of translators
	 */
	List<InstanceTranslator<T>> translators();
	
}
