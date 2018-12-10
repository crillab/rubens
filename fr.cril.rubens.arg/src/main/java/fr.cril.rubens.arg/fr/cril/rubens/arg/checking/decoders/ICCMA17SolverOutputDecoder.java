package fr.cril.rubens.arg.checking.decoders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A solver output decoder corresponding to the outputs of ICCMA17.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ICCMA17SolverOutputDecoder extends AbstractICCMASolverOutputDecoder {
	
	@Override
	protected List<String> readStrExtensionList(final String extensionList) throws SyntaxErrorException {
		final char[] chars = extensionList.toCharArray();
		final SyntaxErrorException e = new SyntaxErrorException("\""+extensionList+"\" is not a valid extension set");
		throwOn(e, chars.length < 2 || chars[0] != '[' || chars[chars.length-1] != ']');
		final List<String> exts = new ArrayList<>();
		int startIndex = -1;
		boolean commaExpected = false;
		for(int i=1; i<chars.length-1; ++i) {
			final char c = chars[i];
			if(commaExpected) {
				throwOn(e, c != ',');
				commaExpected = false;
			} else if(c == '[') {
				throwOn(e, startIndex != -1);
				startIndex = i;
			} else if(c == ']') {
				throwOn(e, startIndex == -1);
				exts.add(new String(Arrays.copyOfRange(chars, startIndex, i+1)));
				commaExpected = true;
				startIndex = -1;
			}
		}
		if(startIndex != -1) {
			throw e;
		}
		return exts;
	}
	
	@Override
	protected String normalizeResult(final String result) {
		return result.replaceAll("\\s", "");
	}
	
	@Override
	protected String normalizeStatusResult(final String result) {
		return normalizeResult(result);
	}
	
	@Override
	protected List<String> splitExtensionSets(final String solverOutput) throws SyntaxErrorException {
		int brackets = 0;
		final char[] chars = solverOutput.replaceAll("\\s", "").toCharArray();
		StringBuilder current = new StringBuilder();
		final List<String> extSets = new ArrayList<>();
		for(int i=0; i<chars.length; ++i) {
			final char c = chars[i];
			if(c == '[') {
				current.append(c);
				brackets++;
			} else if(c==',' && brackets==0) {
				extSets.add(current.toString());
				current = new StringBuilder();
			} else if(brackets == 0) {
				throw new SyntaxErrorException("syntax error: \""+solverOutput+"\" is not a valid result");
			} else {
				current.append(c);
				if(c == ']') {
					brackets--;
				}
			}
		}
		extSets.add(current.toString());
		return extSets;
	}
	
	public List<String> splitDynAcceptanceStatuses(final String solverOutput) throws SyntaxErrorException {
		final String normalized = normalizeResult(solverOutput);
		return Arrays.stream(normalized.split(",")).collect(Collectors.toUnmodifiableList());
	}
	
	public List<String> splitDynExtensions(final String solverOutput) throws SyntaxErrorException {
		return splitExtensionSets(solverOutput);
	}
	
	public List<String> splitDynExtensionSets(final String solverOutput) throws SyntaxErrorException {
		return splitDynExtensions(solverOutput);
	}
	
}
