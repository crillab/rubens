package fr.cril.rubens.testutils;

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

import java.lang.annotation.Annotation;

import fr.cril.rubens.reflection.ReflectorParam;

public class DynamicReflectorParam implements ReflectorParam {

	private final boolean enabled;
	
	private final String name;
	
	private final String family;

	public DynamicReflectorParam(final boolean enabled, final String name) {
		this(enabled, name, "");
	}
	
	public DynamicReflectorParam(final boolean enabled, final String name, final String family) {
		this.enabled = enabled;
		this.name = name;
		this.family = family;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return DynamicReflectorParam.class;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public boolean enabled() {
		return this.enabled;
	}
	
	@Override
	public String family() {
		return this.family;
	}

}
