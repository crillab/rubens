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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

/**
 * The core of an argumentation framework, that is a set of arguments, a set of attacks, and related methods.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public abstract class AArgumentationFrameworkGraph implements Instance {
	
	/** file extension associated with the Aspartix format */
	public static final String APX_EXT = ".apx";
	
	/** file extension associated with the set of extensions */
	public static final String EXTS_EXT = ".exts";
	
	/** the set of arguments involved in the framework */
	protected final ArgumentSet arguments;
	
	/** the set of attacks of the framework */
	protected final AttackSet attacks;
	
	/** the history of translation applied from the initial framework to this one */
	private final List<ArgumentationFrameworkTranslation> translationHistory;
	
	/** the argument under consideration while considering DC/DS queries */
	private Argument argUnderDecision;
	
	/**
	 * Builds an instance of {@link AArgumentationFrameworkGraph} given all its characteristics and its history.
	 * 
	 * @param args the set of arguments
	 * @param attacks the set of attacks
	 * @param translationHistory the translations that was applied to the root instance to get this one
	 */
	protected AArgumentationFrameworkGraph(final ArgumentSet args, final AttackSet attacks, final List<ArgumentationFrameworkTranslation> translationHistory) {
		if(args == null || attacks == null) {
			throw new IllegalArgumentException();
		}
		this.arguments = args;
		this.attacks = attacks;
		this.translationHistory = translationHistory;
	}
	
	/**
	 * Returns the set of arguments contained in this instance.
	 * 
	 * @return the set of arguments contained in this instance
	 */
	public ArgumentSet getArguments() {
		return this.arguments;
	}
	
	/**
	 * Returns the set of attacks contained in this instance.
	 * 
	 * @return the set of attacks contained in this instance
	 */
	public AttackSet getAttacks() {
		return this.attacks;
	}
	
	/**
	 * Returns the translation history from the root instance that have lead to this instance.
	 * 
	 * If this instance was obtained by {@link ArgumentationFramework#ArgumentationFramework(ArgumentSet, AttackSet, ExtensionSet)},
	 * this method will return <code>null</code>.
	 * 
	 * @return the translation history from the root instance that have lead to this instance
	 */
	public List<ArgumentationFrameworkTranslation> getTranslationHistory() {
		return this.translationHistory;
	}
	
	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(APX_EXT, EXTS_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(os == null) {
			throw new IllegalArgumentException();
		}
		if(APX_EXT.equals(extension)) {
			writeInstance(os);
		} else if(EXTS_EXT.equals(extension)) {
			writeExtensions(os);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Writes an AF instance into an output stream using the APX format.
	 * 
	 * @param os the output stream
	 * @throws IOException if an I/O exception occurs
	 */
	protected void writeInstance(final OutputStream os) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			final StringBuilder builder = new StringBuilder();
			this.arguments.stream().map(Argument::getName).forEach(s -> builder.append("arg(").append(s).append(").\n"));
			this.attacks.stream().map(att -> "att("+att.getAttacker().getName()+","+att.getAttacked().getName()+").\n").forEach(builder::append);
			writer.write(builder.toString());
		}
	}
	
	/**
	 * Writes an extension into an output stream.
	 * 
	 * @param os the output stream
	 * @throws IOException if an I/O exception occurs
	 */
	protected abstract void writeExtensions(final OutputStream os) throws IOException;
	
	/**
	 * Translates an extensions into its textual representation.
	 * 
	 * @param extension the extension
	 * @return its textual representation
	 */
	protected String extensionToString(final ArgumentSet extension) {
		return "["+extension.stream().map(Argument::getName).reduce((acc, ext) -> acc+","+ext).orElse("")+"]";
	}
	
	/**
	 * Sets the argument under consideration while considering DC/DS queries.
	 *
	 * @param arg the argument
	 */
	public void setArgUnderDecision(final Argument arg) {
		if(arg == null || !this.arguments.contains(arg)) {
			throw new IllegalArgumentException();
		}
		this.argUnderDecision = arg;
	}
	
	/**
	 * Returns the argument under consideration while considering DC/DS queries.
	 *
	 * @return the argument under consideration while considering DC/DS queries
	 */
	public Argument getArgUnderDecision() {
		return this.argUnderDecision;
	}

}
