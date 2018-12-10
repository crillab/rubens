package fr.cril.rubens.arg.checking.checkers;

import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.reflection.CheckerFactoryReflector;

public class DynamicElementaryCheckersTest {
	
	@Test
	public void testAll() {
		Stream.of("DC", "DS", "EE", "SE").forEach(this::test);
	}
	
	private void test(final String query) {
		Stream.of("CO-D", "GR-D", "PR-D", "ST-D", "SST-D", "STG-D", "ID-D").forEach(s -> test(query, s));
	}
	
	private void test(final String query, final String semantics) {
		CheckerFactoryReflector.getInstance().getClassInstance(query+"-"+semantics);
	}

}
