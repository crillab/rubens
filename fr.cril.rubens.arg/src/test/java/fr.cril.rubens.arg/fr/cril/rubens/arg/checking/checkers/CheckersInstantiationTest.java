package fr.cril.rubens.arg.checking.checkers;

import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.utils.CheckerFactoryReflector;

public class CheckersInstantiationTest {
	
	@Test
	public void testAll() {
		Stream.of("DC", "DS", "EE", "SE").forEach(this::test);
	}
	
	private void test(final String query) {
		Stream.of("CO", "GR", "PR", "ST", "SST", "STG", "ID").forEach(s -> test(query, s));
	}
	
	private void test(final String query, final String semantics) {
		CheckerFactoryReflector.getInstance().getClassInstance(query+"-"+semantics);
	}

}
