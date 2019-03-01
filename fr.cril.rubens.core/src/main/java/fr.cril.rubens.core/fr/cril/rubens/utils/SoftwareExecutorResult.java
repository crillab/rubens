package fr.cril.rubens.utils;

/**
 * A class used to handle results of a software execution.
 * 
 * It keeps the exit status (including timeout) and the contents of both standard and error outputs.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class SoftwareExecutorResult {

	private final int status;

	private final String stdout;

	private final String stderr;

	private final boolean timeouted;

	/**
	 * Builds a result handler given all its characteristics.
	 * 
	 * @param status the exit status
	 * @param timeouted a flag indicated the instance reached the timeout
	 * @param stdout the content of the standard output
	 * @param stderr the content of the error output
	 */
	public SoftwareExecutorResult(final int status, final boolean timeouted, final String stdout, final String stderr) {
		this.status = status;
		this.timeouted = timeouted;
		this.stdout = stdout;
		this.stderr = stderr;
	}

	/**
	 * Returns the exit status
	 * 
	 * @return the exit status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Returns <code>true</code> iff a timeout occurred during execution.
	 * 
	 * @return <code>true</code> iff a timeout occurred during execution
	 */
	public boolean hasTimeouted() {
		return this.timeouted;
	}

	/**
	 * Returns the content of the standard output.
	 * 
	 * @return the content of the standard output
	 */
	public String getStdout() {
		return stdout;
	}

	/**
	 * Returns the content of the error output.
	 * 
	 * @return the content of the error output
	 */
	public String getStderr() {
		return stderr;
	}

}
