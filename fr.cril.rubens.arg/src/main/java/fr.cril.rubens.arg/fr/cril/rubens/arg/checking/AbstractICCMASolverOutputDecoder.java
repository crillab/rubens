package fr.cril.rubens.arg.checking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ExtensionSet;

/**
 * Common code for ICCMA solver output decoders.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public abstract class AbstractICCMASolverOutputDecoder implements ISolverOutputDecoder {
	
	private static final String ACCEPTANCE_TRUE = "YES";
	
	private static final String ACCEPTANCE_FALSE = "NO";
	
	private static final String NO_EXT = "NO";

	@Override
	public boolean isAcceptanceTrue(final String solverOutput) {
		return normalizeStatusResult(solverOutput).equals(ACCEPTANCE_TRUE);
	}
	
	@Override
	public String getAcceptanceTrue() {
		return ACCEPTANCE_TRUE;
	}

	@Override
	public boolean isAcceptanceFalse(final String solverOutput) {
		return normalizeStatusResult(solverOutput).equals(ACCEPTANCE_FALSE);
	}
	
	@Override
	public String getAcceptanceFalse() {
		return ACCEPTANCE_FALSE;
	}

	@Override
	public boolean isNoExt(final String solverOutput) {
		return normalizeStatusResult(solverOutput).equals(NO_EXT);
	}
	
	@Override
	public String getNoExt() {
		return NO_EXT;
	}
	
	@Override
	public ArgumentSet readExtension(final String solverOutput) throws SyntaxErrorException {
		final String normalized = normalizeResult(solverOutput);
		final SyntaxErrorException e = new SyntaxErrorException("\""+normalized+"\" is not a valid extension");
		throwOn(e, normalized.length() < 2 || normalized.charAt(0) != '[' || normalized.charAt(normalized.length()-1) != ']');
		if(normalized.length() == 2) {
			return ArgumentSet.getInstance(Collections.emptySet());
		}
		final List<String> result = Arrays.stream(normalized.substring(1, normalized.length()-1).split(",")).collect(Collectors.toList());
		for(final String arg : result) {
			throwOn(e, arg.chars().anyMatch(c -> !Character.isLetterOrDigit(c)));
		}
		return result.stream().map(Argument::getInstance).collect(ArgumentSet.collector());
	}
	
	@Override
	public ExtensionSet readExtensionSet(final String extensionSet) throws SyntaxErrorException {
		final String normalized = normalizeResult(extensionSet);
		final List<String> strExtensions = readStrExtensionList(normalized);
		final Set<ArgumentSet> extensions = new HashSet<>();
		for(final String strExt : strExtensions) {
			extensions.add(readExtension(strExt));
		}
		return ExtensionSet.getInstance(extensions);
	}
	
	@Override
	public List<ExtensionSet> readD3(final String solverOutput) throws SyntaxErrorException {
		final List<String> extSets = splitExtensionSets(solverOutput);
		if(extSets.size() != 3) {
			throw new SyntaxErrorException("syntax error: \""+solverOutput+"\" is not a valid result");
		}
		final List<ExtensionSet> result = new ArrayList<>();
		for(final String strExtSet : extSets) {
			result.add(readExtensionSet(strExtSet));
		}
		return result;
	}
	
	/**
	 * Throws the provided exception is the provided test succeeds.
	 * 
	 * @param e the exception
	 * @param test the test
	 * @throws SyntaxErrorException if the test succeeds
	 */
	protected void throwOn(final SyntaxErrorException e, final boolean test) throws SyntaxErrorException {
		if(test) {
			throw e;
		}
	}
	
	/**
	 * Reads an extension list and returns each element as a string, in a list.
	 * 
	 * @param extensionList the extension list, as a string
	 * @return a list of the extensions, as strings
	 * @throws SyntaxErrorException if a syntax error in detected
	 */
	protected abstract List<String> readStrExtensionList(final String extensionList) throws SyntaxErrorException;
	
	/**
	 * Normalizes the result for an extension or a set of extensions provided as a string.
	 * 
	 * @param result the result to normalize
	 * @return the normalized string
	 */
	protected abstract String normalizeResult(final String result);
	
	/**
	 * Normalizes the result when is is a simple word (like argument acceptance results).
	 * 
	 * @param result the result to normalize
	 * @return the normalized string
	 */
	protected abstract String normalizeStatusResult(final String result);
	
	/**
	 * Splits a list of extension sets.
	 * 
	 * Extension sets are returned as a list of strings.
	 * 
	 * @param solverOutput the solver output
	 * @return the list of extension sets
	 * @throws SyntaxErrorException if a syntax error in detected
	 */
	protected abstract List<String> splitExtensionSets(final String solverOutput) throws SyntaxErrorException;

}
