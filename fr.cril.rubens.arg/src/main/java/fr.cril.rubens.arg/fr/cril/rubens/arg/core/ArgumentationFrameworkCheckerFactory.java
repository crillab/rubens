package fr.cril.rubens.arg.core;

import fr.cril.rubens.arg.checking.ISolverOutputDecoder;
import fr.cril.rubens.specs.CheckerFactory;

/**
 * An interface for {@link CheckerFactory} instances dedicated to argumentation frameworks.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <R> the type of instances under consideration
 */
public interface ArgumentationFrameworkCheckerFactory<R extends AArgumentationFrameworkGraph> extends CheckerFactory<R> {
	
	/**
	 * Sets the solver output decoder.
	 * 
	 * @param decoder the decoder
	 */
	void setOutputFormat(ISolverOutputDecoder decoder);

}
