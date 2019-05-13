package fr.cril.rubens.arg.core;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation.ArgumentFrameworkAttackTranslation;
import fr.cril.rubens.arg.utils.Forget;

public class ArgumentationFrameworkTranslationTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}

	@Test
	public void testAttackRemoval() {
		final ArgumentFrameworkAttackTranslation translation = ArgumentFrameworkAttackTranslation.attackRemoval(Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
		assertEquals("-att(a,b).", translation.getDescription());
		assertFalse(translation.isNewAttack());
	}
	
	@Test
	public void testNewAttack() {
		final ArgumentFrameworkAttackTranslation translation = ArgumentFrameworkAttackTranslation.newAttack(Attack.getInstance(Argument.getInstance("a"), Argument.getInstance("b")));
		assertEquals("+att(a,b).", translation.getDescription());
		assertTrue(translation.isNewAttack());
	}
}
