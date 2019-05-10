package fr.cril.rubens.options;

import java.util.function.Consumer;

/**
 * A class used to handle methods' specific options.
 * 
 * Options are composed by a key and a value.
 * When the option is present, a specific action is taken, taking the value as parameter.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class MethodOption {
	
	private final String name;
	
	private final String description;
	
	private final Consumer<String> applier;

	/**
	 * Builds a specific option given its name, its description, and the action to take when the option is present.
	 * @param name
	 * @param description
	 * @param applier
	 */
	public MethodOption(final String name, final String description, final Consumer<String> applier) {
		this.name = name;
		this.description = description;
		this.applier = applier;
	}
	
	/**
	 * Returns the name of the option.
	 * 
	 * The name corresponds to the key of the option.
	 * 
	 * @return the name of the option
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the description of the option.
	 * 
	 * This description is displayed when help is required for a its method.
	 * 
	 * @return the description of the option
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Applies the action linked to this option.
	 * 
	 * The action takes the option's value as parameter.
	 * 
	 * @param argument the option's value
	 */
	public void apply(final String argument) {
		this.applier.accept(argument);
	}

}
