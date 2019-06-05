package fr.cril.rubens.cnf.core;

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.utils.WriteUtils;
import fr.cril.rubens.specs.Instance;

/**
 * An {@link Instance} implementation dedicated to the CNF format.
 * 
 * Stores both the CNF formula and its set of models.
 * 
 * Models are stored using a sorted list of models.
 * Each model is represented by a sorted list of integers.
 * Both orders are lexicographic.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class CnfInstance implements Instance {

	/** the default extension associated with a CNF DIMACS problem */ 
	public static final String CNF_EXT = ".cnf";

	/** the default extension associated with a set of models */
	public static final String MODS_EXT = ".mods";

	private final int nVars;

	private final List<List<Integer>> clauses;

	private final List<List<Integer>> models;
	
	/**
	 * Builds a new (trivial) CNF instance containing no variables and no clauses.
	 * 
	 * The set of models is made of a single empty model.
	 */
	public CnfInstance() {
		this.nVars = 0;
		this.clauses = Collections.emptyList();
		this.models = Collections.singletonList(Collections.emptyList());
	}

	/**
	 * The copy constructor of a CNF instance.
	 * 
	 * @param instance the instance to copy
	 */
	public CnfInstance(final CnfInstance instance) {
		this.nVars = instance.nVars;
		this.clauses = instance.clauses();
		this.models = instance.models();
	}
	
	/**
	 * Builds a CNF instance given its number of variables, its clauses, and its models.
	 * 
	 * The models must be given in the lexicographic order.
	 * No check will be executed on the order of the provided models;
	 * Provided them not in a lexicographic order may result in undefined behavior.
	 * 
	 * @param nVars the number of variables
	 * @param clauses the clauses
	 * @param models the (ordered) models
	 */
	public CnfInstance(final int nVars, final List<List<Integer>> clauses, final List<List<Integer>> models) {
		this.nVars = nVars;
		this.clauses = clauses;
		this.models = models;
	}

	/**
	 * Returns the number of variables the CNF admits.
	 * 
	 * @return the number of variables the CNF admits
	 */
	public int nVars() {
		return this.nVars;
	}

	/**
	 * Returns the list of clauses the CNF instance contains.
	 * 
	 * Each clause is defined as a list of literals.
	 * A literal is made of a sign and a variable index.
	 * The sign corresponds to the polarity of the literal.
	 * 
	 * @return the list of clauses the CNF instance contains
	 */
	public List<List<Integer>> clauses() {
		return this.clauses.stream().map(Collections::unmodifiableList).collect(Collectors.toUnmodifiableList());
	}

	/**
	 * Returns the set of models the CNF instance contains.
	 * 
	 * Each model is defined as a list of literals.
	 * A literal is made of a sign and a variable index.
	 * The sign corresponds to the polarity of the literal.
	 * type
	 * @return the set of models the CNF instance contains
	 */
	public List<List<Integer>> models() {
		return this.models.stream().map(Collections::unmodifiableList).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, MODS_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			WriteUtils.writeCNF(this, os);
		} else if(MODS_EXT.equals(extension)) {
			WriteUtils.writeModels(this.models, os);
		} else {
			throw new IllegalArgumentException("unknown extension "+extension);
		}
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clauses == null) ? 0 : clauses.hashCode());
		result = prime * result + ((models == null) ? 0 : models.hashCode());
		result = prime * result + nVars;
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CnfInstance)) {
			return false;
		}
		CnfInstance other = (CnfInstance) obj;
		if (clauses == null) {
			if (other.clauses != null) {
				return false;
			}
		} else if (!clauses.equals(other.clauses)) {
			return false;
		}
		if (models == null) {
			if (other.models != null) {
				return false;
			}
		} else if (!models.equals(other.models)) {
			return false;
		}
		return nVars == other.nVars;
	}
	
	@Override
	public String toString() {
		return "[nVars="+this.nVars+", clauses="+this.clauses+", models="+this.models()+"]";
	}

}
