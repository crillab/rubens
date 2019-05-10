package fr.cril.rubens.options;

import java.util.function.BiConsumer;

/**
 * An common interface for RUBENS applications command line options.
 * 
 * Each option may have a short name, a long name, a description, and may have an argument.
 * See Apache Commons CLI library for more information.
 * 
 * When an option is present, is it applied through a consumer taking an object and the option argument (if one is present).
 * The type of the object passed to the consumer must be passed to the interface.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of the object passed to the consumer
 */
public interface IAppOption<T> {
	
	/**
	 * Returns the current option short name.
	 * 
	 * @return the current option short name
	 */
	public String getOpt();
	
	/**
	 * Returns the current option long name.
	 * 
	 * @return the current option long name
	 */
	public String getLongOpt();
	
	/**
	 * Returns <code>true</code> iff the options has an argument.
	 * 
	 * @return <code>true</code> iff the options has an argument
	 */
	public boolean hasArg();
	
	/**
	 * Returns the current option description.
	 * 
	 * @return the current option description
	 */
	public String getDescription();
	
	/**
	 * Returns the current option consumer.
	 * 
	 * @return the current option consumer.
	 */
	public BiConsumer<T, String> getOptionConsumer();

}
