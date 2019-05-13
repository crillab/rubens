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

import fr.cril.rubens.specs.InstanceTranslator;

public final class EchoTranslators {
	
	private EchoTranslators() {
		// nothing to do here
	}
	
	public static InstanceTranslator<EchoInstance> newA() {
		return new InstanceTranslator<EchoInstance>() {

			@Override
			public boolean canBeAppliedTo(EchoInstance instance) {
				return true;
			}

			@Override
			public EchoInstance translate(EchoInstance instance) {
				return new EchoInstance(instance.toString()+"a");
			}
		};
	}
	
	public static InstanceTranslator<EchoInstance> newB() {
		return new InstanceTranslator<EchoInstance>() {

			@Override
			public boolean canBeAppliedTo(EchoInstance instance) {
				return true;
			}

			@Override
			public EchoInstance translate(EchoInstance instance) {
				return new EchoInstance(instance.toString()+"b");
			}
		};
	}

}
