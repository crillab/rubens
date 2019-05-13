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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

@ReflectorParam(enabled=false)
public class StringConcatGeneratorFactory implements TestGeneratorFactory<StringInstance> {

	@Override
	public StringInstance initInstance() {
		return new StringInstance("");
	}

	@Override
	public List<InstanceTranslator<StringInstance>> translators() {
		return Stream.of(new StringConcatTranslator("1"), new StringConcatTranslator("2")).collect(Collectors.toList());
	}

}
