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
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.specs.InstanceTranslator;

public class StableSemTestGeneratorFactoryTest {
	
	@Test
	public void testInitInstance() {
		assertEquals(new ArgumentationFramework(), new StableSemTestGeneratorFactory().initInstance());
	}
	
	@Test
	public void testTranslators() {
		final List<InstanceTranslator<ArgumentationFramework>> translators = new StableSemTestGeneratorFactory().translators();
		assertEquals(2, translators.size());
		assertTrue(translators.stream().anyMatch(t -> t.getClass().equals(NewArgTranslator.class)));
		assertEquals(1, translators.stream().filter(t -> t.getClass().equals(NewAttackTranslator.class)).map(t -> (NewAttackTranslator) t).map(NewAttackTranslator::getExtensionSetComputer)
				.filter(c -> c == EExtensionSetComputer.STABLE_SEM).count());
	}
	
	@Test
	public void testAddNewArgOnNoExtAF() {
		ArgumentationFramework af = new ArgumentationFramework();
		assertEquals(Stream.of(ArgumentSet.getInstance(Collections.emptySet())).collect(ExtensionSet.collector()), af.getExtensions());
		final NewArgTranslator newArgTranslator = new NewArgTranslator(EExtensionSetComputer.STABLE_SEM);
		af = newArgTranslator.translate(af);
		assertEquals(Stream.of(ArgumentSet.getInstance(Collections.singleton(Argument.getInstance("a0")))).collect(ExtensionSet.collector()), af.getExtensions());
		final NewAttackTranslator newAttackTranslator = new NewAttackTranslator(EExtensionSetComputer.STABLE_SEM);
		af = newAttackTranslator.translate(af);
		assertEquals(ExtensionSet.getInstance(Collections.emptySet()), af.getExtensions());
		af = newArgTranslator.translate(af);
		assertEquals(ExtensionSet.getInstance(Collections.emptySet()), af.getExtensions());
	}

}
