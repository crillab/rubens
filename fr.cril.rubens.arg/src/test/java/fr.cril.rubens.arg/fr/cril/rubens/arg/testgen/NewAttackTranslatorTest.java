package fr.cril.rubens.arg.testgen;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
