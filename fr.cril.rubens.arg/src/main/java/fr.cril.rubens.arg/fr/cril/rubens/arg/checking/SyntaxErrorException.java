package fr.cril.rubens.arg.checking;

/**
 * An exception raised when there is a syntax error in the output of a software.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class SyntaxErrorException extends Exception {
	
	/** the serial verion UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new exception given the reason it was raised.
	 *
	 * @param msg the reason the exception was raised
	 */
	public SyntaxErrorException(final String msg) {
		super(msg);
	}
	
}
