package fr.cril.rubens.arg.testgen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFramework;

public class ASemTestGeneratorFactoryTest {
	
	@Test
	public void testEmptyAfAllowed() {
		final LocalTGF tgf = new LocalTGF(true);
		assertEquals(new ArgumentationFramework(), tgf.initInstance());
	}
	
	@Test
	public void testEmptyAfNotAllowed() {
		final LocalTGF tgf = new LocalTGF(false);
		assertEquals(1, tgf.initInstance().getArguments().size());
	}
	
	private class LocalTGF extends ASemTestGeneratorFactory {
		
		private LocalTGF(final boolean emptyAllowed) {
			super(EExtensionSetComputer.COMPLETE_SEM, emptyAllowed);
		}
	}

}
