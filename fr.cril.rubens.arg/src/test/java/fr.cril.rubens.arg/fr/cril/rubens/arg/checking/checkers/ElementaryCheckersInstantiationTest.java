package fr.cril.rubens.arg.checking.checkers;

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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.cril.rubens.reflection.CheckerFactoryReflector;

class ElementaryCheckersInstantiationTest {
	
	@Test
	void testAll() {
		Stream.of("DC", "DS", "EE", "CE", "SE").forEach(this::test);
	}
	
	private void test(final String query) {
		Stream.of("CO", "GR", "PR", "ST", "SST", "STG", "ID").forEach(s -> test(query, s));
	}
	
	private void test(final String query, final String semantics) {
		CheckerFactoryReflector.getInstance().getClassInstance(query+"-"+semantics);
	}

}
