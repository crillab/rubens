package fr.cril.rubens.arg.testgen;

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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An instance translator that adds new arguments to an existing frameworks.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewArgTranslator implements InstanceTranslator<ArgumentationFramework> {
	
	/** the integer index used to build argument names */
	private int nextArgIndex = 0;
	
	/** the algorithm used to compute extensions */
	private EExtensionSetComputer extensionSetComputer;
	
	/**
	 * Builds a new instance of this translator given the algorithm used to compute extensions.
	 * 
	 * @param extensionSetComputer the algorithm used to compute extensions
	 */
	public NewArgTranslator(final EExtensionSetComputer extensionSetComputer) {
		this.extensionSetComputer = extensionSetComputer;
	}

	@Override
	public boolean canBeAppliedTo(final ArgumentationFramework instance) {
		return true;
	}

	@Override
	public ArgumentationFramework translate(final ArgumentationFramework instance) {
		Argument newArg0 = null;
		while(newArg0 == null) {
			final Argument a = Argument.getInstance("a"+(this.nextArgIndex++));
			if(!instance.getArguments().contains(a)) {
				newArg0 = a;
			}
		}
		final Argument newArg = newArg0;
		final ArgumentSet newArguments = Stream.concat(instance.getArguments().stream(), Stream.of(newArg)).collect(ArgumentSet.collector());
		final ExtensionSet newExtensions = this.extensionSetComputer.compute(newArguments, instance.getAttacks());
		final ArgumentationFramework af = new ArgumentationFramework(newArguments, instance.getAttacks(), newExtensions, instance, ArgumentationFrameworkTranslation.newArgument(newArg));
		final List<Argument> argList = newArguments.stream().collect(Collectors.toList());
		Collections.shuffle(argList);
		af.setArgUnderDecision(argList.get(0));
		return af;
	}

}
