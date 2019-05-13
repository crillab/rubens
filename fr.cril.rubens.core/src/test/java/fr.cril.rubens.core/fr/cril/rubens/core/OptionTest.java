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

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.options.MethodOption;

public class OptionTest {
	
	private MethodOption opt;
	
	private String str;
	
	@Before
	public void setUp() {
		this.str = "";
		this.opt = new MethodOption("n", "d", v -> this.str = v);
	}
	
	@Test
	public void testName() {
		assertEquals("n", this.opt.getName());
	}
	
	@Test
	public void testDescription() {
		assertEquals("d", this.opt.getDescription());
	}
	
	@Test
	public void testApply() {
		this.opt.apply("ok");
		assertEquals("ok", this.str);
	}

}
