package fr.cril.rubens.core;

/**
 * A class used to handle results of a checking process executed on an instance.
 * It handles both the result (success / fail) and a message corresponding to the error, if any.
 * 
 * An instance is dedicated to successful checks and can be got using {@link CheckResult#SUCCESS}.
 * In case of check fails, call {@link CheckResult#newError(String)} with the explanation to get an instance of this class.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class CheckResult {
	
	/** a flag indicating the check was successful */
	private final boolean isSuccesful;
	
	/** the explanation message */
	private final String explanation;
	
	/** the instance corresponding to a success */
	public static final CheckResult SUCCESS = new CheckResult(true, "SUCCESS");
	
	/**
	 * Builds an instance given the success status and the explanation message.
	 * 
	 * @param isSuccesful the success status
	 * @param explanation the explanation message
	 */
	private CheckResult(final boolean isSuccesful, final String explanation) {
		this.isSuccesful = isSuccesful;
		this.explanation = explanation;
	}
	
	/**
	 * Returns <code>true</code> iff the check was successful.
	 * 
	 * @return <code>true</code> iff the check was successful
	 */
	public boolean isSuccessful() {
		return this.isSuccesful;
	}
	
	/**
	 * Returns the explanation of the error.
	 * 
	 * @return the explanation of the error
	 */
	public String getExplanation() {
		return this.explanation;
	}
	
	/**
	 * Builds a new instance of this class for check fails.
	 * 
	 * @param explanation the fail explanation
	 * @return the corresponding {@link CheckResult} instance
	 */
	public static CheckResult newError(final String explanation) {
		return new CheckResult(false, explanation);
	}
	
	/**
	 * Builds an exception encapsulating the current erroneous check result.
	 * 
	 * If this method is called on a successful result, an {@link UnsupportedOperationException} is throw.
	 * 
	 * @return an exception encapsulating the current erroneous check result
	 */
	public ResultIsErrorException asException() {
		if(this == SUCCESS) {
			throw new UnsupportedOperationException();
		}
		return new ResultIsErrorException(this.explanation);
	}
	
	/**
	 * A class used to return check fails as exceptions.
	 * 
	 * @author Emmanuel Lonca - lonca@cril.fr
	 */
	public class ResultIsErrorException extends Exception {

		private static final long serialVersionUID = 1L;
		
		private final String explanation;
		
		/**
		 * Builds an exception given the reason the result is an error.
		 * 
		 * @param explanation the reason the result is an error
		 */
		public ResultIsErrorException(final String explanation) {
			this.explanation = explanation;
		}
		
		/**
		 * Returns the error as a {@link CheckResult}.
		 * 
		 * @return the (erroneous) result
		 */
		public CheckResult getErrorResult() {
			return CheckResult.newError(this.explanation);
		}
		
	}

}
