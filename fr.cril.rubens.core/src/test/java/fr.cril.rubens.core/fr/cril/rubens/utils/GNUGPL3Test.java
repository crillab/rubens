package fr.cril.rubens.utils;

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

import org.junit.Test;
import org.slf4j.LoggerFactory;

public class GNUGPL3Test {
	
	@Test
	public void testWelcomeMessage() {
		GNUGPL3.logWelcomeMessage(LoggerFactory.getLogger(GNUGPL3Test.class));
	}
	
	@Test
	public void testLicense() {
		GNUGPL3.logTermsAndConditions(LoggerFactory.getLogger(GNUGPL3Test.class));
	}

}
