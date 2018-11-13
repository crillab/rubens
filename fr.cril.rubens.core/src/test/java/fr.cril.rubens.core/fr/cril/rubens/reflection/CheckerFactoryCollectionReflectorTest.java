package fr.cril.rubens.reflection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckerFactoryCollectionReflectorTest {
	
	@Test
	public void testGetInstance() {
		final CheckerFactoryCollectionReflector reflector = CheckerFactoryCollectionReflector.getInstance();
		assertTrue(reflector == CheckerFactoryCollectionReflector.getInstance());
	}

}
