package fr.cril.rubens.arg.checking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A solver output decoder corresponding to the outputs of ICCMA17.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ICCMA19SolverOutputDecoder extends AbstractICCMASolverOutputDecoder {
	
	@Override
	protected List<String> readStrExtensionList(final String extensionList) throws SyntaxErrorException {
		final List<String> lines = Arrays.stream(extensionList.split("\n")).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		if(lines.size() == 1 && lines.get(0).equals("[]")) {
			return Collections.emptyList();
		}
		final SyntaxErrorException e = new SyntaxErrorException("\""+extensionList+"\" is not a valid extension set");
		throwOn(e, lines.size() < 2 || !lines.get(0).equals("[") || !lines.get(lines.size()-1).equals("]"));
		return IntStream.range(1, lines.size()-1).mapToObj(lines::get).collect(Collectors.toList());
	}
	
	@Override
	protected String normalizeResult(final String result) {
		return Arrays.stream(result.split("\n")).filter(l -> !l.isEmpty()).reduce((a,b) -> a+"\n"+b).orElse("").replaceAll("[ \t]*", "");
	}
	
	@Override
	protected String normalizeStatusResult(final String result) {
		return result.replaceAll("\\s", "");
	}
	
	@Override
	protected List<String> splitExtensionSets(final String solverOutput) throws SyntaxErrorException {
		final List<String> lines = Arrays.stream(normalizeResult(solverOutput).split("\n")).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		final List<String> result = new ArrayList<>();
		final List<String> currentExtSet = new ArrayList<>();
		int brackets = 0;
		final SyntaxErrorException e = new SyntaxErrorException("syntax error: \""+solverOutput+"\" is not a valid result");
		for(final String line : lines) {
			currentExtSet.add(line);
			if(line.equals("[")) {
				++brackets;
			} else if("]".equals(line)) {
				--brackets;
				if(brackets > 0) {
					continue;
				}
				if(brackets < 0) {
					throw e;
				}
				result.add(currentExtSet.stream().reduce((a,b) -> a+"\n"+b).orElseThrow());
				currentExtSet.clear();
			} else if("[]".equals(line) && brackets == 0) {
				result.add(line);
				currentExtSet.clear();
			} else if(brackets == 0) {
				throw e;
			}
		}
		if(brackets != 0) {
			throw e;
		}
		return result;
	}
	
}
