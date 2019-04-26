package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class TrueNodeTest {
	
	@Test
	public void testModels() {
		assertEquals(Collections.singletonList(Collections.emptyMap()), new TrueNode(1).models());
	}

}
