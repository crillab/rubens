package fr.cril.rubens.arg.checking.decoders;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
		final List<String> lines = Arrays.stream(extensionList.split("\n")).collect(Collectors.toList());
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
		final List<String> lines = Arrays.stream(normalizeResult(solverOutput).split("\n")).collect(Collectors.toList());
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
	
	@Override
	public List<String> splitDynAcceptanceStatuses(final String solverOutput) throws SyntaxErrorException {
		final String normalized = normalizeStatusResult(solverOutput);
		final int lastIndex = normalized.length()-1;
		if(normalized.length() < 2 || normalized.charAt(0) != '[' || normalized.charAt(lastIndex) != ']') {
			throw new SyntaxErrorException("syntax error: \""+solverOutput+"\" is not a valid result");
		}
		return Arrays.stream(normalized.substring(1, lastIndex).split(",")).collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public List<String> splitDynExtensions(final String solverOutput) throws SyntaxErrorException {
		final List<String> lines = Arrays.stream(solverOutput.split("\n")).collect(Collectors.toList());
		if(lines.size() < 2 || !lines.get(0).equals("[") || !lines.get(lines.size()-1).equals("]")) {
			throw new SyntaxErrorException("syntax error in dynamic extension list: "+solverOutput);
		}
		return lines.subList(1, lines.size()-1).stream().collect(Collectors.toList());
	}
	
	@Override
	public List<String> splitDynExtensionSets(final String solverOutput) throws SyntaxErrorException {
		final List<String> lines = Arrays.stream(solverOutput.split("\n")).collect(Collectors.toList());
		if(lines.size() < 2 || !lines.get(0).equals("[") || !lines.get(lines.size()-1).equals("]")) {
			throw new SyntaxErrorException("syntax error in dynamic extension list: "+solverOutput);
		}
		return splitExtensionSets(lines.subList(1, lines.size()-1).stream().reduce((a,b) -> a+"\n"+b).orElseThrow());
	}
	
}
