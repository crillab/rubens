package fr.cril.rubens.arg.testgen;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.utils.Forget;

class NewAttackTranslatorTest {
	
	private NewAttackTranslator translator;
	
	private Argument arg1;

	private Attack att11;
	
	private ArgumentationFramework afArg1;
	
	private ArgumentationFramework afArg1Att11;

	@BeforeEach
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
	void testCanBeAppliedEmptyInstance() {
		assertFalse(this.translator.canBeAppliedTo(new ArgumentationFramework()));
	}
	
	@Test
	void testCanBeApplied() {
		assertTrue(this.translator.canBeAppliedTo(afArg1));
	}
	
	@Test
	void testCannotBeAppliedNoAutoAttacks() {
		this.translator.setAutoAttacksAllowed(false);
		assertFalse(this.translator.canBeAppliedTo(afArg1));
	}
	
	@Test
	void testCannotBeApplied() {
		assertFalse(this.translator.canBeAppliedTo(this.afArg1Att11));
	}
	
	@Test
	void testTranslateWhenCannotBeApplied() {
		assertThrows(IllegalStateException.class, () -> this.translator.translate(this.afArg1Att11));
	}
	
	@Test
	void testTranslateEmptyAf() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertThrows(IllegalStateException.class, () -> this.translator.translate(af));
	}
	
	@Test
	void testNewAtt() {
		ArgumentationFramework actual = this.translator.translate(this.afArg1);
		assertEquals(this.afArg1Att11, actual);
	}
	
	@Test
	void testAddAllAttacks() {
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
	void testAddAllAttacksNoAutoAttacks() {
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
	void testGetExtensionComputer() {
		assertEquals(EExtensionSetComputer.COMPLETE_SEM, this.translator.getExtensionSetComputer());
	}

}
