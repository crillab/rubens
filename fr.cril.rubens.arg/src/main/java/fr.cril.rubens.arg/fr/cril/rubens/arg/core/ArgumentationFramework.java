package fr.cril.rubens.arg.core;

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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class used to handle argumentation frameworks.
 * 
 * In case a framework is the result of applying a sequence of translations from the empty framework,
 * it is advised to instantiate it using the {@link ArgumentationFramework#ArgumentationFramework(ArgumentSet, AttackSet, ExtensionSet, ArgumentationFramework, ArgumentationFrameworkTranslation)}
 * constructor to build the new one; this will keep the translation history.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ArgumentationFramework extends AArgumentationFrameworkGraph {
	
	/** the set of extensions associated with this framework */
	private final ExtensionSet extensions;
	
	/**
	 * Builds a new empty instance of {@link ArgumentationFramework}.
	 */
	public ArgumentationFramework() {
		this(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))),
				Stream.of(ArgumentationFrameworkTranslation.emptyAF()).collect(Collectors.toUnmodifiableList()));
	}
	
	/**
	 * Builds a new argumentation frameworks given all its characteristics.
	 * 
	 * @param arguments the set of arguments
	 * @param attacks the set of attacks
	 * @param extensions the set of extensions
	 */
	public ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions) {
		this(arguments, attacks, extensions, null);
	}
	
	/**
	 * Builds a new argumentation frameworks given all its characteristics and the translation that created it.
	 * 
	 * In case a framework is the result of applying a sequence of translations from the empty framework,
	 * it is advised to instantiate it using this constructor to build the new one; this will keep the translation history.
	 * 
	 * @param arguments the set of arguments
	 * @param attacks the set of attacks
	 * @param extensions the set of extensions
	 * @param oldFramework the previous argumentation framework
	 * @param translation the translation used to translate the old framework to this new one
	 */
	public ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions, final ArgumentationFramework oldFramework,
			final ArgumentationFrameworkTranslation translation) {
		this(arguments, attacks, extensions, oldFramework.getTranslationHistory() == null ? null :
			Stream.concat(oldFramework.getTranslationHistory().stream(), Stream.of(translation)).collect(Collectors.toUnmodifiableList()));
	}
	
	private ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions, List<ArgumentationFrameworkTranslation> history) {
		super(arguments, attacks, history);
		if(extensions == null) {
			throw new IllegalArgumentException();
		}
		this.extensions = extensions;
	}

	@Override
	protected void writeExtensions(final OutputStream os) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			final Optional<String> content = this.extensions.stream().map(this::extensionToString).reduce((acc, ext) -> acc+",\n"+ext);
			if(!content.isPresent()) {
				writer.write("[\n]\n");
			} else {
				writer.write("[\n");
				writer.write(content.get());
				writer.write("\n]\n");
			}
		}
	}
	
	/**
	 * Returns the set of extensions this framework admits.
	 * 
	 * @return the set of extensions this framework admits
	 */
	public ExtensionSet getExtensions() {
		return this.extensions;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arguments.hashCode();
		result = prime * result + attacks.hashCode();
		result = prime * result + extensions.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArgumentationFramework)) {
			return false;
		}
		final ArgumentationFramework other = (ArgumentationFramework) obj;
		return this.arguments.equals(other.arguments) && this.attacks.equals(other.attacks) && this.extensions.equals(other.extensions);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(this.arguments);
		final String comma = ", ";
		builder.append(comma);
		builder.append(this.attacks);
		builder.append(comma);
		builder.append(this.extensions);
		builder.append(']');
		return builder.toString();
	}

}
