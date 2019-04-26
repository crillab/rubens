package fr.cril.rubens.cnf.ddnnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DDNNFReader {
	
	private int nDeclaredNodes;
	
	private int nDeclaredEdges;
	
	private int nEdges;
	
	private int nDeclaredVars;
	
	private List<String> lines;
	
	private INode[] nodes;
	
	public DDNNF read(final String content) throws DDNNFException {
		this.lines = Arrays.stream(content.split("\n")).collect(Collectors.toList());
		while(!this.lines.isEmpty() && this.lines.get(this.lines.size()-1).isEmpty()) {
			this.lines.remove(this.lines.size()-1);
		}
		parsePreamble();
		if(lines.size()-1 != this.nDeclaredNodes) {
			throw DDNNFException.newWrongNumberOfNodesInPreamble(this.nDeclaredNodes, lines.size()-1);
		}
		for(int i=1; i<lines.size(); ++i) {
			parseNode(i-1);
		}
		if(this.nDeclaredEdges != this.nEdges) {
			throw DDNNFException.newWrongNumberOfEdgesInPreamble(this.nDeclaredEdges, this.nEdges);
		}
		return this.nodes.length == 0 ? new DDNNF(this.nDeclaredVars) : new DDNNF(this.nDeclaredVars, this.nodes[this.nDeclaredNodes-1]);
	}

	private void parsePreamble() throws DDNNFException {
		if(lines.isEmpty()) {
			throw DDNNFException.newSyntaxErrorInPreamble();
		}
		final String[] words = this.lines.get(0).replaceAll("[ \t]+", " ").split(" ");
		if(words.length != 4 || !words[0].equals("nnf")) {
			throw DDNNFException.newSyntaxErrorInPreamble();
		}
		try {
			this.nDeclaredNodes = Integer.parseInt(words[1]);
			this.nodes = new INode[this.nDeclaredNodes];
			Arrays.fill(this.nodes, null);
			this.nDeclaredEdges = Integer.parseInt(words[2]);
			this.nEdges = 0;
			this.nDeclaredVars = Integer.parseInt(words[3]);
		} catch(NumberFormatException e) {
			throw DDNNFException.newSyntaxErrorInPreamble();
		}
	}
	
	private void parseNode(final int nodeIndex) throws DDNNFException {
		final String[] words = lines.get(nodeIndex + 1).replaceAll("[ \t]+", " ").split(" ");
		switch(words[0]) {
		case "A":
			parseAndNode(words, nodeIndex);
			break;
		case "O":
			parseOrNode(words, nodeIndex);
			break;
		case "L":
			parseLiteralNode(words, nodeIndex);
			break;
		default:
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
	}
	
	private void parseAndNode(final String[] words, final int nodeIndex) throws DDNNFException {
		if(words.length < 2) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		int nArgs = readPosIntParam(words[1], nodeIndex);
		this.nEdges += nArgs;
		if(words.length != 2 + nArgs) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		if(nArgs == 0) {
			this.nodes[nodeIndex] = new TrueNode(nodeIndex);
		} else {
			final List<INode> children = new ArrayList<>();
			for(int i=2; i<words.length; ++i) {
				children.add(this.nodes[readChildNodeIndex(nodeIndex, words, i)]);
			}
			this.nodes[nodeIndex] = new DecomposableAndNode(nodeIndex, children);
		}
	}

	private void parseOrNode(final String[] words, final int nodeIndex) throws DDNNFException {
		if(words.length < 3) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		int cflVar = readPosIntParam(words[1], nodeIndex);
		int nArgs = readPosIntParam(words[2], nodeIndex);
		this.nEdges += nArgs;
		if(words.length != 3 + nArgs) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		if(nArgs == 0) {
			if(cflVar != 0) {
				throw DDNNFException.newErrorAtNode(nodeIndex);
			}
			this.nodes[nodeIndex] = new FalseNode(nodeIndex);
		} else if(nArgs == 2) {
			if(cflVar < 1 || cflVar > this.nDeclaredVars) {
				throw DDNNFException.newErrorAtNode(nodeIndex);
			}
			this.nodes[nodeIndex] = new DeterministicOrNode(nodeIndex, cflVar, this.nodes[readChildNodeIndex(nodeIndex, words, 3)], this.nodes[readChildNodeIndex(nodeIndex, words, 4)]);
		} else {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
	}
	
	private void parseLiteralNode(final String[] words, final int nodeIndex) throws DDNNFException {
		if(words.length != 2) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		final int lit = readIntParam(words[1], nodeIndex);
		if(lit == 0 || Math.abs(lit) > this.nDeclaredVars) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		this.nodes[nodeIndex] = new LiteralNode(nodeIndex, lit);
	}
	
	private int readChildNodeIndex(final int nodeIndex, final String[] words, int wordIndex) throws DDNNFException {
		int index = readPosIntParam(words[wordIndex], nodeIndex);
		if(index >= nodeIndex) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		return index;
	}
	
	private int readPosIntParam(final String word, final int nodeIndex) throws DDNNFException {
		final int param = readIntParam(word, nodeIndex);
		if(param < 0) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
		return param;
	}
	
	private int readIntParam(final String word, final int nodeIndex) throws DDNNFException {
		try {
			return Integer.parseInt(word);
		} catch(NumberFormatException e) {
			throw DDNNFException.newErrorAtNode(nodeIndex);
		}
	}

}