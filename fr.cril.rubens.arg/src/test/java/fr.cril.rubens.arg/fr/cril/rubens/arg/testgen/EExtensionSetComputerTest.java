package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;

public class EExtensionSetComputerTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}
	
	@Test
	public void testEmptyInstanceCO() {
		final ExtensionSet exts = EExtensionSetComputer.COMPLETE_SEM.compute(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()));
		assertEquals(1, exts.size());
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	public void testCO() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		final Attack att12 = Attack.getInstance(arg1, arg2);
		final Attack att21 = Attack.getInstance(arg2, arg1);
		final ExtensionSet exts = EExtensionSetComputer.COMPLETE_SEM.compute(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(att12, att21).collect(AttackSet.collector()));
		assertEquals(3, exts.size());
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.emptySet())));
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg1))));
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg2))));
	}
	
	@Test
	public void testEmptyInstancePR() {
		final ExtensionSet exts = EExtensionSetComputer.PREFERRED_SEM.compute(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()));
		assertEquals(1, exts.size());
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	public void testPR() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		final Attack att12 = Attack.getInstance(arg1, arg2);
		final Attack att21 = Attack.getInstance(arg2, arg1);
		final ExtensionSet exts = EExtensionSetComputer.PREFERRED_SEM.compute(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(att12, att21).collect(AttackSet.collector()));
		assertEquals(2, exts.size());
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg1))));
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg2))));
	}
	
	@Test
	public void testEmptyInstanceST() {
		final ExtensionSet exts = EExtensionSetComputer.STABLE_SEM.compute(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()));
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), exts);
	}
	
	@Test
	public void testST() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		final Attack att12 = Attack.getInstance(arg1, arg2);
		final Attack att21 = Attack.getInstance(arg2, arg1);
		final ExtensionSet exts = EExtensionSetComputer.STABLE_SEM.compute(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(att12, att21).collect(AttackSet.collector()));
		assertEquals(2, exts.size());
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg1))));
		assertTrue(exts.contains(ArgumentSet.getInstance(Collections.singleton(arg2))));
	}
	
	@Test
	public void testEmptyInstanceGR() {
		final ExtensionSet exts = EExtensionSetComputer.GROUNDED_SEM.compute(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()));
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), exts);
	}
	
	@Test
	public void testEmptyGR() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		final Attack att12 = Attack.getInstance(arg1, arg2);
		final Attack att21 = Attack.getInstance(arg2, arg1);
		final ExtensionSet exts = EExtensionSetComputer.GROUNDED_SEM.compute(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(att12, att21).collect(AttackSet.collector()));
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), exts);
	}
	
	@Test
	public void testGR() {
		final Argument arg1 = Argument.getInstance("a1");
		final Argument arg2 = Argument.getInstance("a2");
		final Attack att12 = Attack.getInstance(arg1, arg2);
		final ExtensionSet exts = EExtensionSetComputer.GROUNDED_SEM.compute(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(att12).collect(AttackSet.collector()));
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(arg1)))), exts);
	}

}
