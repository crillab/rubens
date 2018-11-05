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
import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.arg.utils.Forget;

public class NewAttackTranslatorTest {
	
	private NewAttackTranslator translator;
	
	private Argument arg1;

	private Attack att11;
	
	private ArgumentationFramework afArg1;
	
	private ArgumentationFramework afArg1Att11;

	@Before
	public void setUp() {
		Forget.all();
		this.arg1 = Argument.getInstance("a1");
		this.att11 = Attack.getInstance(this.arg1, this.arg1);
		final ArgumentSet argSet1 = ArgumentSet.getInstance(Collections.singleton(this.arg1));
		this.afArg1 = new ArgumentationFramework(argSet1, AttackSet.getInstance(Collections.emptySet()), Stream.of(argSet1).collect(ExtensionSet.collector()));
		this.afArg1Att11 = new ArgumentationFramework(argSet1, AttackSet.getInstance(Collections.singleton(this.att11)), Stream.of(ArgumentSet.getInstance(Collections.emptySet())).collect(ExtensionSet.collector()));
		this.translator = new NewAttackTranslator(EExtensionSetComputer.COMPLETE_SEM);
	}
	
	@Test
	public void testCanBeAppliedEmptyInstance() {
		assertFalse(this.translator.canBeAppliedTo(new ArgumentationFramework()));
	}
	
	@Test
	public void testCanBeApplied() {
		assertTrue(this.translator.canBeAppliedTo(afArg1));
	}
	
	@Test
	public void testCannotBeAppliedNoAutoAttacks() {
		this.translator.setAutoAttacksAllowed(false);
		assertFalse(this.translator.canBeAppliedTo(afArg1));
	}
	
	@Test
	public void testCannotBeApplied() {
		assertFalse(this.translator.canBeAppliedTo(this.afArg1Att11));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testTranslateWhenCannotBeApplied() {
		this.translator.translate(this.afArg1Att11);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testTranslateEmptyAf() {
		this.translator.translate(new ArgumentationFramework());
	}
	
	@Test
	public void testNewAtt() {
		ArgumentationFramework actual = this.translator.translate(this.afArg1);
		assertEquals(this.afArg1Att11, actual);
	}
	
	@Test
	public void testAddAllAttacks() {
		final Argument a1 = Argument.getInstance("a1");
		final Argument a2 = Argument.getInstance("a2");
		final ArgumentationFramework af = new ArgumentationFramework(Stream.of(a1, a2).collect(ArgumentSet.collector()), AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.emptySet()));
		ArgumentationFramework currentAf = af;
		for(int i=0; i<4; ++i) {
			assertTrue(this.translator.canBeAppliedTo(currentAf));
			currentAf = this.translator.translate(currentAf);
		}
		assertFalse(this.translator.canBeAppliedTo(currentAf));
	}
	
	@Test
	public void testAddAllAttacksNoAutoAttacks() {
		this.translator.setAutoAttacksAllowed(false);
		final Argument a1 = Argument.getInstance("a1");
		final Argument a2 = Argument.getInstance("a2");
		final ArgumentationFramework af = new ArgumentationFramework(Stream.of(a1, a2).collect(ArgumentSet.collector()), AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.emptySet()));
		ArgumentationFramework currentAf = af;
		for(int i=0; i<2; ++i) {
			assertTrue(this.translator.canBeAppliedTo(currentAf));
			currentAf = this.translator.translate(currentAf);
		}
		assertFalse(this.translator.canBeAppliedTo(currentAf));
	}
	
	@Test
	public void testGetExtensionComputer() {
		assertEquals(EExtensionSetComputer.COMPLETE_SEM, this.translator.getExtensionSetComputer());
	}

}
