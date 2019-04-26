package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class DecomposableAndNodeTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoChildren() throws DDNNFException {
		new DecomposableAndNode(1, Collections.emptyList());
	}
	
	@Test(expected=DDNNFException.class)
	public void testNotDecomposable() throws DDNNFException {
		new DecomposableAndNode(1, Stream.of(new LiteralNode(1, 1), new LiteralNode(2, -1)).collect(Collectors.toList()));
	}
	
	@Test
	public void testModels() throws DDNNFException {
		final DecomposableAndNode node = new DecomposableAndNode(Utils.nextIndex(), Stream.of(Utils.buildXor(1, 2), Utils.buildXor(3, 4)).collect(Collectors.toList()));
		assertEquals(Stream.of(
				Utils.buildModel(1, -2, 3, -4),
				Utils.buildModel(1, -2, -3, 4),
				Utils.buildModel(-1, 2, 3, -4),
				Utils.buildModel(-1, 2, -3, 4)
		).collect(Collectors.toSet()), new HashSet<>(node.models()));
	}
	
}
