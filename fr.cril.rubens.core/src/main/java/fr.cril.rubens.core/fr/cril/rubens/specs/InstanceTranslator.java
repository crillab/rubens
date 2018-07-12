package fr.cril.rubens.specs;

/**
 * An interface for translation rules.
 * Translation rules are algorithms intended to translate an instance into another instance of the same type given a predefined pattern.
 * 
 * An instance translator may not be able to translate any input instance. Before trying to apply the translation using {@link InstanceTranslator#translate(Instance)},
 * the user must call {@link InstanceTranslator#canBeAppliedTo(Instance)}  to check if the instance is handled.
 * 
 * Instance translators MUST override {@link Object#hashCode()} and {@link Object#equals(Object)}, since they may be mapped to other objects.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of instances to translate
 */
public interface InstanceTranslator<T extends Instance> {
	
	/**
	 * Indicates whether or not the translation may be applied to the provided instance.
	 * 
	 * @param instance the instance
	 * @return <code>true</code> iff the translation may be applied
	 */
	boolean canBeAppliedTo(T instance);
	
	/**
	 * Translates the input instance into an output one, using the semantic of the implementor.
	 * 
	 * The translation process may be unavailable for some input instances.
	 * The user must call {@link InstanceTranslator#canBeAppliedTo(Instance)} to ensure the translation is permitted.
	 * 
	 * In case this method is called on an input instance which is not handle, the behavior is undefined.
	 * 
	 * @param instance the input instance
	 * @return the generated new instance
	 */
	T translate(T instance);

}
