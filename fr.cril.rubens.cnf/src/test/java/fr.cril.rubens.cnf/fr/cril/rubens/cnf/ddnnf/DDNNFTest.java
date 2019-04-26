package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
