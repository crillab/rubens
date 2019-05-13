package fr.cril.rubens.arg.core;

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation.ArgumentFrameworkAttackTranslation;

/**
 * A dynamic argumentation framework.
 * 
 * A dynamic argumentation framework is a list of argumentation framework, such that passing from one to the next is made by adding or removing an attack.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DynamicArgumentationFramework extends AArgumentationFrameworkGraph {
	
	private final ArgumentationFramework initInstance;
	
	private List<Translation> translations = new ArrayList<>();
	
	/** file extension associated with dynamics in Aspartix format */
	public static final String APXM_EXT = ".apxm";
	
	/**
	 * Builds a dynamic argumentation framework given the initial instance.
	 * 
	 * @param initInstance the initial instance
	 */
	public DynamicArgumentationFramework(final ArgumentationFramework initInstance) {
		super(initInstance.getArguments(), initInstance.getAttacks(), initInstance.getTranslationHistory());
		this.initInstance = initInstance;
	}
	
	/**
	 * Adds a translation got by adding an attack.
	 * 
	 * @param attack the new attack
	 * @param af the new argumentation framework
	 */
	public void addTranlationNewAttack(final Attack attack, final ArgumentationFramework af) {
		this.translations.add(new Translation(ArgumentFrameworkAttackTranslation.newAttack(attack), af));
	}
	
	/**
	 * Adds a translation got by removing an attack.
	 * 
	 * @param attack the removed attack
	 * @param af the new argumentation framework
	 */
	public void addTranslationAttackRemoval(final Attack attack, final ArgumentationFramework af) {
		this.translations.add(new Translation(ArgumentFrameworkAttackTranslation.attackRemoval(attack), af));
	}
	
	/**
	 * Returns the initial instance.
	 * 
	 * @return the initial instance
	 */
	public ArgumentationFramework getInitInstance() {
		return this.initInstance;
	}
	
	/**
	 * Returns the list of translations.
	 * 
	 * @return the list of translations
	 */
	public List<Translation> getTranslations() {
		return Collections.unmodifiableList(this.translations);
	}
	
	/**
	 * A couple (attack translation, induced AF).
	 * 
	 * @author Emmanuel Lonca - lonca@cril.fr
	 */
	public class Translation {
		
		private final ArgumentFrameworkAttackTranslation attackTranslation;
		
		private final ArgumentationFramework af;

		/**
		 * Builds a translation given the attack translation and the induced argumentation framework.
		 * 
		 * @param translation the attack translation
		 * @param af the argumentation framework
		 */
		public Translation(final ArgumentFrameworkAttackTranslation translation, final ArgumentationFramework af) {
			this.attackTranslation = translation;
			this.af = af;
		}
		
		/**
		 * Returns the translation description.
		 * 
		 * @return the translation description
		 */
		public ArgumentFrameworkAttackTranslation getTranslation() {
			return this.attackTranslation;
		}
		
		/**
		 * Returns the argumentation framework obtained by the translation.
		 * 
		 * @return the argumentation framework obtained by the translation
		 */
		public ArgumentationFramework getArgumentationFramework() {
			return this.af;
		}
		
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.concat(Stream.of(AArgumentationFrameworkGraph.APX_EXT, APXM_EXT, AArgumentationFrameworkGraph.EXTS_EXT),
				IntStream.range(0, translations.size()).mapToObj(i -> AArgumentationFrameworkGraph.EXTS_EXT+"."+(i+1)))
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(APXM_EXT.equals(extension)) {
			final StringBuilder sbuf = new StringBuilder();
			this.translations.stream().map(t -> t.attackTranslation.getDescription()).forEach(m -> {
				sbuf.append(m);
				sbuf.append('\n');
			});
			os.write(sbuf.toString().getBytes());
		} else if(extension.startsWith(AArgumentationFrameworkGraph.EXTS_EXT+".")) {
			try {
				final int index = Integer.parseInt(extension.substring(1+extension.indexOf('.', 1)))-1;
				this.translations.get(index).af.write(AArgumentationFrameworkGraph.EXTS_EXT, os);
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException(extension+" is not a valid dynamic AF file extension");
			}
		} else {
			this.initInstance.write(extension, os);
		}
	}

	@Override
	protected void writeExtensions(OutputStream os) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(this.initInstance.getArguments());
		final String comma = ", ";
		builder.append(comma);
		builder.append(this.initInstance.getAttacks());
		builder.append(comma);
		builder.append(this.translations.stream().map(t -> t.attackTranslation.getDescription()).reduce((a,b)->a+","+b).orElse(""));
		builder.append(comma);
		builder.append(this.initInstance.getExtensions());
		this.translations.stream().map(t -> t.getArgumentationFramework().getExtensions()).forEach(exts -> {
			builder.append(comma);
			builder.append(exts);
		});
		builder.append(']');
		return builder.toString();
	}

}
