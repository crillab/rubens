package fr.cril.rubens.cnf.ddnnf;

public class DDNNFException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private DDNNFException(final String msg) {
		super(msg);
	}
	
	public static DDNNFException newSyntaxErrorInPreamble() {
		return new DDNNFException("syntax error in preamble");
	}
	
	public static DDNNFException newErrorAtNode(final int nodeIndex) {
		return new DDNNFException("an error was detected at node which index is "+nodeIndex);
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
