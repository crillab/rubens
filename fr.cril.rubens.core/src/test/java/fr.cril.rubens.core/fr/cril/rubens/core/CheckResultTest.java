package fr.cril.rubens.core;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CheckResultTest {
	
	@Test
	void testSuccessfulInstance() {
		assertTrue(CheckResult.SUCCESS.isSuccessful());
		assertEquals("SUCCESS", CheckResult.SUCCESS.getExplanation());
	}
	
	@Test
	void testFailedInstance() {
		final CheckResult result = CheckResult.newError("foobar");
		assertFalse(result.isSuccessful());
		assertEquals("foobar", result.getExplanation());
	}
	
	@Test
	void testAsException() {
		final CheckResult result = CheckResult.newError("foobar");
		assertEquals("foobar", result.asException().getErrorResult().getExplanation());
	}
	
	@Test
	void testSuccessAsException() {
		assertThrows(UnsupportedOperationException.class, () -> CheckResult.SUCCESS.asException());
	}

}
