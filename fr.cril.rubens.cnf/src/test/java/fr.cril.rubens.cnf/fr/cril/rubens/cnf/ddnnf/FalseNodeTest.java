package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class FalseNodeTest {
	
	@Test
	public void testModels() {
		assertEquals(Collections.emptyList(), new FalseNode(1).models());
	}

}
