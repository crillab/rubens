package fr.cril.rubens.cnf.ddnnf;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

public class DDNNFException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private DDNNFException(final String msg) {
		super(msg);
	}
	
	public static DDNNFException newSyntaxErrorInPreamble() {
		return new DDNNFException("syntax error in preamble");
	}
	
	public static DDNNFException newErrorAtNode(final int nodeIndex, final String reason) {
		return new DDNNFException("an error was detected at node which index is "+nodeIndex+" ("+reason+")");
	}
	
	public static DDNNFException newWrongNumberOfNodesInPreamble(final int declared, final int effective) {
		return new DDNNFException("wrong number of declared nodes (declared: "+declared+"; effective: "+effective+")");
	}
	
	public static DDNNFException newWrongNumberOfEdgesInPreamble(final int declared, final int effective) {
		return new DDNNFException("wrong number of declared edges (declared: "+declared+"; effective: "+effective+")");
	}
	
	public static DDNNFException newNotDecomposableAndNode(final int nodeIndex) {
		return new DDNNFException("AND node at index "+nodeIndex+" is not decomposable");
	}
	
	public static DDNNFException newNotDeterministOrNode(final int nodeIndex) {
		return new DDNNFException("OR node at index "+nodeIndex+" is not determinist");
	}

}
