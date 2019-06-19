package fr.cril.rubens.generator;

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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class EGeneratorOptionTest {
	
	@Test
	public void testOptionsNames() {
		final Set<String> genOptNames = Arrays.stream(EGeneratorOption.values()).map(o -> o.getSpecs().getOpt()).collect(Collectors.toSet());
		final Set<String> apacheOptNames = EGeneratorOption.buildCliOptions().getOptions().stream().map(Option::getOpt).collect(Collectors.toSet());
		assertEquals(apacheOptNames, genOptNames);
	}
	
	@Test
	public void testNoNullConsumer() {
		assertTrue(Arrays.stream(EGeneratorOption.values()).map(EGeneratorOption::getOptionConsumer).noneMatch(Objects::isNull));
	}

}
