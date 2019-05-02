package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DDNNFExceptionTest {
	
	@Test
	public void testNewSyntaxErrorInPreamble() {
		assertEquals("syntax error in preamble", DDNNFException.newSyntaxErrorInPreamble().getMessage());
	}
	
	@Test
	public void testNewErrorAtNode() {
		assertEquals("an error was detected at node which index is 1 (foo)", DDNNFException.newErrorAtNode(1, "foo").getMessage());
	}
	
	@Test
	public void testNewWrongNumberOfNodesInPreamble() {
		assertEquals("wrong number of declared nodes (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfNodesInPreamble(1, 2).getMessage());
	}
	
	@Test
	public void testNewWrongNumberOfEdgesInPreamble() {
		assertEquals("wrong number of declared edges (declared: 1; effective: 2)", DDNNFException.newWrongNumberOfEdgesInPreamble(1, 2).getMessage());
	}
	
	@Test
	public void testNewNotDecomposableAndNode() {
		assertEquals("AND node at index 1 is not decomposable", DDNNFException.newNotDecomposableAndNode(1).getMessage());
	}
	
	@Test
	public void testNewNotDeterministOrNode() {
		assertEquals("OR node at index 1 is not determinist", DDNNFException.newNotDeterministOrNode(1).getMessage());
	}

}
