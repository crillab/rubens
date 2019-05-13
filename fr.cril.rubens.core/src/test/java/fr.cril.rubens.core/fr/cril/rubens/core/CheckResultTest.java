package fr.cril.rubens.core;

/*-
 * #%L
 * RUBENS core API
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckResultTest {
	
	@Test
	public void testSuccessfulInstance() {
		assertTrue(CheckResult.SUCCESS.isSuccessful());
		assertEquals("SUCCESS", CheckResult.SUCCESS.getExplanation());
	}
	
	@Test
	public void testFailedInstance() {
		final CheckResult result = CheckResult.newError("foobar");
		assertFalse(result.isSuccessful());
		assertEquals("foobar", result.getExplanation());
	}
	
	@Test
	public void testAsException() {
		final CheckResult result = CheckResult.newError("foobar");
		assertEquals("foobar", result.asException().getErrorResult().getExplanation());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testSuccessAsException() {
		CheckResult.SUCCESS.asException();
	}

}
