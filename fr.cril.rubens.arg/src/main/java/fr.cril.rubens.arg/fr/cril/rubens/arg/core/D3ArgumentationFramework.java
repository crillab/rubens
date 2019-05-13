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
import java.util.List;

import fr.cril.rubens.arg.testgen.EExtensionSetComputer;

/**
 * A special argumentation framework used for the D3 ("Dung's Triathlon") query.
 * 
 * Instead of having one extension set, this AF contains three of them (one related to the EE-GR query, one for the EE-ST query, and one for the EE-PR query). 
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class D3ArgumentationFramework extends AArgumentationFrameworkGraph {
	
	/** the grounded extensions of the AF */
	private final ExtensionSet grExts;
	
	/** the stable extensions of the AF */
	private final ExtensionSet stExts;
	
	/** the preferred extensions of the AF */
	private final ExtensionSet prExts;

	/**
	 * Builds an argumentation framework for the D3 query given all its characteristics and its history.
	 * 
	 * @param args the set of arguments
	 * @param attacks the set of attacks
	 * @param grExts the GR extensions
	 * @param stExts the ST extensions
	 * @param prExts the PR extension
	 * @param translationHistory the translations that was applied to the root instance to get this one
	 */
	public D3ArgumentationFramework(final ArgumentSet args, final AttackSet attacks, final ExtensionSet grExts, final ExtensionSet stExts, final ExtensionSet prExts,
			final List<ArgumentationFrameworkTranslation> translationHistory) {
		super(args, attacks, translationHistory);
		this.grExts = grExts;
		this.stExts = stExts;
		this.prExts = prExts;
	}
	
	/**
	 * Builds an argumentation framework for the D3 query from a classical {@link ArgumentationFramework}.
	 * 
	 * The set of arguments and attacks are copied while the extensions set for GR, ST and PR are computed.
	 * 
	 * @param af the classical AF
	 */
	public D3ArgumentationFramework(final ArgumentationFramework af) {
		this(af.getArguments(), af.getAttacks(), EExtensionSetComputer.GROUNDED_SEM.compute(af.getArguments(), af.getAttacks()),
				EExtensionSetComputer.STABLE_SEM.compute(af.getArguments(), af.getAttacks()),
				EExtensionSetComputer.PREFERRED_SEM.compute(af.getArguments(), af.getAttacks()), af.getTranslationHistory());
	}

	@Override
	protected void writeExtensions(final OutputStream os) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			writer.write('[');
			writer.write(this.grExts.stream().map(this::extensionToString).reduce((acc, ext) -> acc+","+ext).orElse(""));
			writer.write("],[");
			writer.write(this.stExts.stream().map(this::extensionToString).reduce((acc, ext) -> acc+","+ext).orElse(""));
			writer.write("],[");
			writer.write(this.prExts.stream().map(this::extensionToString).reduce((acc, ext) -> acc+","+ext).orElse(""));
			writer.write("]\n");
		}
	}

	/**
	 * Returns the grounded extensions.
	 * 
	 * @return the grounded extensions
	 */
	public ExtensionSet getGrExts() {
		return grExts;
	}

	/**
	 * Returns the stable extensions.
	 * 
	 * @return the stable extensions
	 */
	public ExtensionSet getStExts() {
		return stExts;
	}

	/**
	 * Returns the preferred extensions.
	 * 
	 * @return the preferred extensions
	 */
	public ExtensionSet getPrExts() {
		return prExts;
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
		builder.append(this.grExts);
		builder.append(comma);
		builder.append(this.stExts);
		builder.append(comma);
		builder.append(this.prExts);
		builder.append(']');
		return builder.toString();
	}

}
