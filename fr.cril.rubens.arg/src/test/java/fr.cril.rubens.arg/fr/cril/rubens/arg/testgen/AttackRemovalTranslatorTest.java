package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;

public class AttackRemovalTranslatorTest {
	
	private AttackRemovalTranslator translator;
	
	private ArgumentationFramework af1;
	
	private ArgumentationFramework af2;

	private Attack attack1;
	
	private ArgumentationFramework translated1;

	@Before
	public void setUp() {
		Forget.all();
		this.translator = new AttackRemovalTranslator(EExtensionSetComputer.COMPLETE_SEM);
		final Argument arg1 = Argument.getInstance("a");
		final Argument arg2 = Argument.getInstance("b");
		this.attack1 = Attack.getInstance(arg1, arg1);
		final Attack attack2 = Attack.getInstance(arg2, arg2);
		this.af1 = new ArgumentationFramework(ArgumentSet.getInstance(Collections.singleton(arg1)), AttackSet.getInstance(Collections.singleton(attack1)),
				ExtensionSet.getInstance(Collections.emptySet()));
		this.af2 = new ArgumentationFramework(Stream.of(arg1, arg2).collect(ArgumentSet.collector()), Stream.of(attack1, attack2).collect(AttackSet.collector()),
				ExtensionSet.getInstance(Collections.emptySet()));
		this.translated1 = new ArgumentationFramework(ArgumentSet.getInstance(Collections.singleton(arg1)), AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(arg1)))));
	}
	
	@Test
	public void testCannotBeApplied() {
		assertFalse(this.translator.canBeAppliedTo(new ArgumentationFramework()));
	}
	
	@Test
	public void testCanBeApplied() {
		assertTrue(this.translator.canBeAppliedTo(this.af1));
	}
	
	@Test
	public void testSelectAttackToRemove() {
		assertEquals(this.attack1, this.translator.selectAttackToRemove(this.af1));
	}
	
	@Test
	public void testTranslate1() {
		final Attack att = this.translator.selectAttackToRemove(this.af1);
		assertEquals(this.translated1, this.translator.translate(this.af1, att));
	}
	
	@Test
	public void testTranslate2() {
		assertEquals(this.translated1, this.translator.translate(this.af1));
	}
	
	@Test
	public void testTranslate3() {
		assertEquals(1, this.translator.translate(this.af2).getExtensions().size());
	}

}
