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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;

class D3TestGeneratorFactoryTest {
	
	@Test
	void testInit() {
		final D3TestGeneratorFactory factory = new D3TestGeneratorFactory();
		assertEquals(ArgumentSet.getInstance(Collections.emptySet()), factory.initInstance().getArguments());
		assertEquals(2, factory.translators().size());
	}
	
	@Test
	void testTranslateSome() {
		final D3TestGeneratorFactory factory = new D3TestGeneratorFactory();
		final Set<D3ArgumentationFramework> generated = new HashSet<>();
		generated.add(factory.initInstance());
		for(final InstanceTranslator<D3ArgumentationFramework> translator: factory.translators()) {
			final List<D3ArgumentationFramework> newAFs = generated.stream().filter(i -> translator.canBeAppliedTo(i)).map(translator::translate).collect(Collectors.toList());
			generated.addAll(newAFs);
		}
		for(final InstanceTranslator<D3ArgumentationFramework> translator: factory.translators()) {
			final List<D3ArgumentationFramework> newAFs = generated.stream().filter(i -> translator.canBeAppliedTo(i)).map(translator::translate).collect(Collectors.toList());
			generated.addAll(newAFs);
		}
		assertTrue(generated.size() > 1);
	}

}
