package fr.cril.rubens.arg.checking.decoders;

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

import org.junit.Test;

public class SolverOutputDecoderFactoryTest {
	
	@Test
	public void testGetICCMA17ByName() {
		assertEquals(ICCMA17SolverOutputDecoder.class, SolverOutputDecoderFactory.getInstanceByName("ICCMA17").getDecoderInstance().getClass());
	}
	
	@Test
	public void testGetICCMA19ByName() {
		assertEquals(ICCMA19SolverOutputDecoder.class, SolverOutputDecoderFactory.getInstanceByName("ICCMA19").getDecoderInstance().getClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetByWrongName() {
		SolverOutputDecoderFactory.getInstanceByName("Foo");
	}

}
