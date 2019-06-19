package fr.cril.rubens.utils;

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

import org.junit.Test;
import org.slf4j.LoggerFactory;

public class LoggerHelperTest {
	
	@Test
	public void testDefaultLogger() {
		assertEquals(LoggerFactory.getLogger("RUBENS"), LoggerHelper.getInstance().getLogger());
	}
	
	@Test
	public void testSwitchLogger() {
		final LoggerHelper helper = LoggerHelper.getInstance();
		helper.switchLogger("foo");
		assertEquals(LoggerFactory.getLogger("foo"), helper.getLogger());
	}

}