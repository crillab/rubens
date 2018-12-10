package fr.cril.rubens.arg.core;

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
