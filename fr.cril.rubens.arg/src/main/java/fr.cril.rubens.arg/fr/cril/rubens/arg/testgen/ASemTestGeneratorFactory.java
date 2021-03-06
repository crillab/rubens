package fr.cril.rubens.arg.testgen;

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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * Common code for abstract argumentation based test generator factories.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(enabled=false)
public class ASemTestGeneratorFactory implements TestGeneratorFactory<ArgumentationFramework> {
	
	private final EExtensionSetComputer extensionSetComputer;
	
	private final ArgumentationFramework initInstance;
	
	private NewArgTranslator newArgTranslator;

	/**
	 * Builds a new abstract argumentation based test generator factory.
	 * 
	 * A flag is used to allow/disallow the empty argumentation framework.
	 * In case it is allowed, it is the root instance.
	 * In the other case, the root instance is the one having a single argument and no attacks.
	 * 
	 * @param extensionSetComputer the extension set computer
	 * @param emptyExtensionAllowed the flag used to allow/disallow the empty argumentation framework
	 */
	protected ASemTestGeneratorFactory(final EExtensionSetComputer extensionSetComputer, final boolean emptyExtensionAllowed) {
		this.extensionSetComputer = extensionSetComputer;
		final ArgumentationFramework emptyInstance = new ArgumentationFramework();
		this.newArgTranslator = new NewArgTranslator(extensionSetComputer);
		this.initInstance = emptyExtensionAllowed ? emptyInstance : this.newArgTranslator.translate(emptyInstance);
	}
	
	@Override
	public ArgumentationFramework initInstance() {
		return this.initInstance;
	}

	@Override
	public List<InstanceTranslator<ArgumentationFramework>> translators() {
		final List<InstanceTranslator<ArgumentationFramework>> result = Stream.of(this.newArgTranslator, new NewAttackTranslator(this.extensionSetComputer)).collect(Collectors.toList());
		this.newArgTranslator = new NewArgTranslator(this.extensionSetComputer);
		return result;
	}

}
