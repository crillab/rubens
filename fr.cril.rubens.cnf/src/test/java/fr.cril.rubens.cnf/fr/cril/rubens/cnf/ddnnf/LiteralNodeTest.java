package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class LiteralNodeTest {
	
	@Test
	public void testPositiveLiteral() {
		assertEquals(Collections.singletonList(Collections.singletonMap(1, true)), new LiteralNode(1, 1).models());
	}
	
	@Test
	public void testNegativeLiteral() {
		assertEquals(Collections.singletonList(Collections.singletonMap(1, false)), new LiteralNode(1, -1).models());
	}

}
