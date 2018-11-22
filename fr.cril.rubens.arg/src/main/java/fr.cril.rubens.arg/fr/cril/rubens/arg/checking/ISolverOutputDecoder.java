package fr.cril.rubens.arg.checking;

import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ExtensionSet;

/**
 * An interface used to read solver outputs.
 * 
 * As multiple solver output coexists, there is a need for an interface here.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public interface ISolverOutputDecoder {
	
	/**
	 * Returns <code>true</code> iff the solver output corresponds to the positive acceptance status.
	 * 
	 * @param solverOutput the solver output
	 * @return <code>true</code> iff the solver output corresponds to the positive acceptance status
	 */
	boolean isAcceptanceTrue(final String solverOutput);
	
	/**
	 * Returns the string corresponding to the positive acceptance status.
	 * 
	 * @return the string corresponding to the positive acceptance status
	 */
	String getAcceptanceTrue();
	
	/**
	 * Returns <code>true</code> iff the solver output corresponds to the negative acceptance status.
	 * 
	 * @param solverOutput the solver output
	 * @return <code>true</code> iff the solver output corresponds to the negative acceptance status
	 */
	boolean isAcceptanceFalse(final String solverOutput);
	
	/**
	 * Returns the string corresponding to the negative acceptance status.
	 * 
	 * @return the string corresponding to the negative acceptance status
	 */
	String getAcceptanceFalse();
	
	/**
	 * Returns <code>true</code> iff the solver output corresponds to no extension.
	 * 
	 * @param solverOutput the solver output
	 * @return <code>true</code> iff the solver output corresponds to no extension
	 */
	boolean isNoExt(final String solverOutput);
	
	/**
	 * Returns the string corresponding to no extension.
	 * 
	 * @return the string corresponding to no extension
	 */
	String getNoExt();
	
	/**
	 * Decodes a solver output corresponding to a single extension.
	 * 
	 * @param solverOutput the solver output
	 * @return the extension
	 * @throws SyntaxErrorException iff the output contains syntax error while considering it contains a single extension
	 */
	ArgumentSet readExtension(final String solverOutput) throws SyntaxErrorException;
	
	/**
	 * Decodes a solver output corresponding to an extension set.
	 * 
	 * @param solverOutput the solver output
	 * @return the extension set
	 * @throws SyntaxErrorException iff the output contains syntax error while considering it contains an extension set
	 */
	ExtensionSet readExtensionSet(final String extensionSet) throws SyntaxErrorException;

}
