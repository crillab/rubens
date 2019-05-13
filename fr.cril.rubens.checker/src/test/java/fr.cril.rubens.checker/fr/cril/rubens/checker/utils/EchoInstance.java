package fr.cril.rubens.checker.utils;

/*-
 * #%L
 * RUBENS solver checker
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

public class EchoInstance implements Instance {
	
	private final String str;

	public EchoInstance(final String str) {
		this.str = str;
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(".txt").collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(!extension.equals(".txt")) {
			return;
		}
		os.write(this.str.getBytes());
	}
	
	@Override
	public String toString() {
		return this.str;
	}

}
