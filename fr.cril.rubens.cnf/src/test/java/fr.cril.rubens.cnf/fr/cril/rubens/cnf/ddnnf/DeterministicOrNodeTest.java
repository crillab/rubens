package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class DeterministicOrNodeTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic1() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(2, 1), new LiteralNode(3, 1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic2() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, new LiteralNode(Utils.nextIndex(), 1), Utils.buildXor(1, 2));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic3() throws DDNNFException {
		new DeterministicOrNode(Utils.nextIndex(), 2, Utils.buildXor(1, 2), new LiteralNode(Utils.nextIndex(), 1));
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDeterministic4() throws DDNNFException {
		new DeterministicOrNode(1, 1, new LiteralNode(2, 1), new LiteralNode(3, 2));
	}
	
	@Test
	public void testModels() {
		assertEquals(Stream.of(Utils.buildModel(-1, 2), Utils.buildModel(1, -2)).collect(Collectors.toSet()), new HashSet<>(Utils.buildXor(1, 2).models()));
	}
	
	@Test
	public void testOrNodeHasFalseAsChild() throws DDNNFException {
		final INode falseNode = new FalseNode(0);
		final INode trueNode = new TrueNode(1);
		final INode litNode2 = new LiteralNode(2, -1);
		final INode andNode3 = new DecomposableAndNode(3, Stream.of(litNode2, trueNode).collect(Collectors.toList()));
		final INode andNode4 = new DecomposableAndNode(4, Stream.of(falseNode).collect(Collectors.toList()));
		new DeterministicOrNode(5, 1, andNode4, andNode3);
	}
	
}
