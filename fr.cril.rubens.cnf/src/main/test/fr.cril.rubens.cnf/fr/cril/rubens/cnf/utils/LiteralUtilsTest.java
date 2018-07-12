package fr.cril.rubens.cnf.utils;

import static org.junit.Assert.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class LiteralUtilsTest {

	@Test
	public void testDimacsToInternal() {
		assertEquals(0, LiteralUtils.dimacsToInternal(1));
		assertEquals(1, LiteralUtils.dimacsToInternal(-1));
		assertEquals(2, LiteralUtils.dimacsToInternal(2));
		assertEquals(3, LiteralUtils.dimacsToInternal(-2));
	}

	@Test
	public void testSelectRandomLiteral() {
		for(int i=0; i<10; ++i) {
			assertEquals(1, LiteralUtils.selectRandomLiteral(Stream.of(0, 1, 0).collect(Collectors.toList())));
		}

	}
}
