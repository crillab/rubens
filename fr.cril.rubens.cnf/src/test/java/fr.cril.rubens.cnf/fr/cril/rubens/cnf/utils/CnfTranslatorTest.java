package fr.cril.rubens.cnf.utils;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.core.NewVarTranslator;

class CnfTranslatorTest {
	
	private NewVarTranslator adapted;
	
	private CnfTranslatorAdapter<CnfInstance, CnfInstance> adapter;

	@BeforeEach
	public void setUp() {
		this.adapted = new NewVarTranslator();
		this.adapter = new CnfTranslatorAdapter<>(this.adapted);
	}
	
	@Test
	void testCanBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		assertTrue(this.adapted.canBeAppliedTo(instance));
		assertTrue(this.adapter.canBeAppliedTo(instance));
	}
	
	@Test
	void testCannotBeApplied() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList()),
				Stream.of(-1).collect(Collectors.toList())).collect(Collectors.toList()), Collections.emptyList());
		assertFalse(this.adapted.canBeAppliedTo(instance));
		assertFalse(this.adapter.canBeAppliedTo(instance));
	}
	
	@Test
	void testApply() {
		final CnfInstance instance = new CnfInstance(1, Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()),
				Stream.of(Stream.of(1).collect(Collectors.toList())).collect(Collectors.toList()));
		final CnfInstance newInstanceOfAdapted = this.adapted.translate(instance);
		final CnfInstance newInstanceOfAdapter = this.adapter.translate(instance);
		assertEquals(newInstanceOfAdapted, newInstanceOfAdapter);
	}
	
	@Test
	void testIllegalAdapted() {
		final CnfTranslatorAdapter<CnfInstance, DummyCnfInstance> dummyAdapter = new CnfTranslatorAdapter<CnfInstance, DummyCnfInstance>(this.adapted);
		DummyCnfInstance cnf = new DummyCnfInstance();
		assertThrows(IllegalArgumentException.class, () -> dummyAdapter.translate(cnf));
	}
	
	private class DummyCnfInstance extends CnfInstance {
		
		private DummyCnfInstance() {
			super();
		}
		
	}

}
