package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.NewArgTranslator;
import fr.cril.rubens.arg.utils.Forget;

public class NewArgTranslatorTest {
	
	private NewArgTranslator translator;
	
	private ArgumentationFramework af;

	private Set<Attack> atts;

	private Set<Set<Argument>> exts;

	@Before
	public void setUp() {
		Forget.all();
		this.translator = new NewArgTranslator(EExtensionSetComputer.COMPLETE_SEM);
		final Argument argA = Argument.getInstance("a0");
		final Argument argB = Argument.getInstance("a1");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		this.atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		this.exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		this.af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
	}
	
	@Test
	public void testCanBeAppliedEmptyInstance() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertTrue(this.translator.canBeAppliedTo(af));
	}
	
	@Test
	public void testTranslateEmptyInstance() {
		final ArgumentationFramework af = new ArgumentationFramework();
		final ArgumentationFramework newAf = this.translator.translate(af);
		final Argument arg = Argument.getInstance("a0");
		assertEquals(Stream.of(arg).collect(ArgumentSet.collector()), newAf.getArguments());
		assertEquals(AttackSet.getInstance(Collections.emptySet()), newAf.getAttacks());
		assertEquals(Stream.of(Stream.of(arg).collect(ArgumentSet.collector())).collect(ExtensionSet.collector()), newAf.getExtensions());
	}
	
	@Test
	public void testCanBeApplied() {
		assertTrue(this.translator.canBeAppliedTo(this.af));
	}
	
	@Test
	public void testTranslate() {
		final ArgumentationFramework newAf = this.translator.translate(this.af);
		final Argument newArg = Argument.getInstance("a2");
		assertEquals(Stream.of(Argument.getInstance("a0"), Argument.getInstance("a1"), newArg).collect(ArgumentSet.collector()), newAf.getArguments());
		assertEquals(AttackSet.getInstance(this.atts), newAf.getAttacks());
		assertEquals(Stream.of(Stream.concat(this.exts.iterator().next().stream(), Stream.of(newArg)).collect(ArgumentSet.collector())).collect(ExtensionSet.collector()), newAf.getExtensions());
	}

}
