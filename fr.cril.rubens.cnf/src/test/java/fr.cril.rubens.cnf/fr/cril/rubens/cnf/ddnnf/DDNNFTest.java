package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class DDNNFTest {
	
	@Before
	public void setUp() {
		Utils.resetNextIndex();
	}
	
	@Test
	public void testModels() {
		final DDNNF ddnnf = new DDNNF(2, Utils.buildXor(1, 2));
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testModelsWithFreeVars() {
		final DDNNF ddnnf = new DDNNF(4, Utils.buildXor(1, 2));
		assertEquals(8, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testModelsWithFreeVars2() throws DDNNFException {
		final INode trueNode = new TrueNode(1);
		final INode litNode = new LiteralNode(2, -1);
		final INode andNode = new DecomposableAndNode(3, Stream.of(litNode, trueNode).collect(Collectors.toList()));
		final DDNNF ddnnf = new DDNNF(3, andNode);
		final Set<Set<Integer>> actual = new HashSet<>(Utils.models(ddnnf));
		final Set<Set<Integer>> expected = Stream.of(
			Stream.of(-1, -2, -3).collect(Collectors.toSet()),
			Stream.of(-1, -2, 3).collect(Collectors.toSet()),
			Stream.of(-1, 2, -3).collect(Collectors.toSet()),
			Stream.of(-1, 2, 3).collect(Collectors.toSet())
		).collect(Collectors.toSet());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNoConstraints() {
		final DDNNF ddnnf = new DDNNF(1);
		final List<Set<Integer>> models = Utils.models(ddnnf);
		assertEquals(2, models.size());
		assertTrue("models should contain [1] (models are "+models+")", models.contains(Collections.singleton(1)));
		assertTrue("models should contain [-1] (models are "+models+")", models.contains(Collections.singleton(-1)));
	}
	
	@Test
	public void testNoVars() {
		final DDNNF ddnnf = new DDNNF(0, new TrueNode(0));
		assertEquals(Collections.singletonList(Collections.emptySet()), Utils.models(ddnnf));
	}
	
}
