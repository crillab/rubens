package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.checking.checkers.ElementaryCheckers;

public class CheckerOptionsApplierTest {
	
	private ArgumentationFrameworkCheckerFactory<ArgumentationFramework> factory;
	
	@Before
	public void setUp() {
		this.factory = new ElementaryCheckers.EECOChecker();
	}
	
	@Test
	public void testOutputFormat() {
		assertTrue(CheckerOptionsApplier.apply("outputFormat=ICCMA17", this.factory));
		final ArgumentSet argSet = Stream.of(Argument.getInstance("a")).collect(ArgumentSet.collector());
		final ArgumentationFramework af = new ArgumentationFramework(argSet, AttackSet.getInstance(Collections.emptySet()), Stream.of(argSet).collect(ExtensionSet.collector()));
		assertTrue(this.factory.checkSoftwareOutput(af, "[[a]]").isSuccessful());
	}
	
	@Test
	public void testSyntaxErrorOptions() {
		assertFalse(CheckerOptionsApplier.apply("outputFormat+ICCMA17", this.factory));
	}
	
	@Test
	public void testUnknownOptions() {
		assertFalse(CheckerOptionsApplier.apply("outputFormatt=ICCMA17", this.factory));
	}

}
