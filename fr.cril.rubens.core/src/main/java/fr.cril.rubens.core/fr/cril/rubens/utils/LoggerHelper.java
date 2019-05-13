package fr.cril.rubens.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class used in RUBENS to get slf4j loggers.
 * 
 * This class implements the singleton design pattern; use {@link LoggerHelper#getInstance()} to get its instance.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class LoggerHelper {
	
	private static LoggerHelper instance = null;
	
	private Logger logger;
	
	/**
	 * Returns the helper instance.
	 * 
	 * On first call, the current logger is set to its default value (the logger referred as "RUBENS").
	 * 
	 * @return the helper instance
	 */
	public static synchronized LoggerHelper getInstance() {
		if(instance == null) {
			instance = new LoggerHelper();
		}
		return instance;
	}
	
	private LoggerHelper() {
		this.logger = LoggerFactory.getLogger("RUBENS");
	}
	
	/**
	 * Returns the current logger.
	 * 
	 * @return the current logger
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * Changes the current logger.
	 * 
	 * The new current logger is the one named after the parameter.
	 * Its name must be compatible with the ones expected by {@link LoggerFactory#getLogger(String)}.
	 * 
	 * @param name the name of the new current logger
	 * @return the new current logger
	 */
	public Logger switchLogger(final String name) {
		this.logger = LoggerFactory.getLogger(name);
		return this.logger;
	}

}
