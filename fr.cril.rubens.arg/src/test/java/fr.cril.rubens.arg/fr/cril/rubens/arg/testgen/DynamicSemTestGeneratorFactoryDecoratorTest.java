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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;


public class DynamicSemTestGeneratorFactoryDecoratorTest {
	
	private DynamicSemTestGeneratorFactoryDecorator decorator;
	
	private CompleteSemTestGeneratorFactory decorated;
	
	@Before
	public void setUp() {
		this.decorated = new CompleteSemTestGeneratorFactory();
		this.decorator = new DynamicSemTestGeneratorFactoryDecorator(this.decorated, EExtensionSetComputer.COMPLETE_SEM);
	}
	
	@Test
	public void testInitInstance() {
		assertEquals(this.decorated.initInstance(), this.decorator.initInstance().getInitInstance());
	}
	
	@Test
	public void testTranslators() {
		assertEquals(this.decorated.translators().size(), this.decorator.translators().size());
	}
	
	@Test
	public void testNoDyn() {
		final CompleteSemTestGeneratorFactory decorated = new CompleteSemTestGeneratorFactory();
		int index = -1;
		final List<InstanceTranslator<ArgumentationFramework>> decoratedTranslators = decorated.translators();
		for(int i=0; i<decoratedTranslators.size(); ++i) {
			if(decoratedTranslators.get(i).getClass().equals(NewArgTranslator.class)) {
				index = i;
				break;
			}
		}
		assertNotEquals(-1, index);
		final DynamicSemTestGeneratorFactoryDecorator decorator = new DynamicSemTestGeneratorFactoryDecorator(decorated, EExtensionSetComputer.COMPLETE_SEM);
		final InstanceTranslator<DynamicArgumentationFramework> argTranslator = decorator.translators().get(index);
		final InstanceTranslator<DynamicArgumentationFramework> attTranslator = decorator.translators().get(1-index);
		for(int i=0; i<(1<<8); ++i) {
			assertTrue(argTranslator.canBeAppliedTo(decorator.initInstance()));
			final DynamicArgumentationFramework generated = argTranslator.translate(decorator.initInstance());
			assertEquals(1, generated.getTranslations().size());
			assertTrue(attTranslator.canBeAppliedTo(generated));
			final DynamicArgumentationFramework generated2 = attTranslator.translate(generated);
			assertEquals(1, generated2.getTranslations().size());
		}
	}

}
