package fr.cril.rubens.cnf.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

/**
 * An {@link Instance} implementation dedicated to the CNF format.
 * 
 * Stores both the CNF formula and its set of models.
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

	private final Set<Set<Integer>> models;
	
	/**
	 * Builds a new (trivial) CNF instance containing no variables and no clauses.
	 * 
	 * The set of models is made of a single empty model.
	 */
	public CnfInstance() {
		this.nVars = 0;
		this.clauses = Collections.emptyList();
		this.models = Stream.of(Collections.unmodifiableSet(new HashSet<Integer>())).collect(Collectors.toSet());
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
	
	public CnfInstance(final int nVars, final List<List<Integer>> clauses, final Set<Set<Integer>> models) {
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
	public Set<Set<Integer>> models() {
		return this.models.stream().map(Collections::unmodifiableSet).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, MODS_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			writeCNF(os);
		} else if(MODS_EXT.equals(extension)) {
			writeModels(os);
		} else {
			throw new IllegalArgumentException("unknown extension "+extension);
		}
	}

	private void writeModels(final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			for(final Set<Integer> cl : this.models) {
				writeTuple(writer, cl);
			}
			writer.flush();
		}
	}

	/**
	 * Writes the CNF into the specified output stream.
	 * 
	 * The CNF is written using the DIMACS format.
	 * 
	 * @param os the output stream
	 * @throws IOException if an {@link IOException} occurs while writing the CNF
	 */
	protected void writeCNF(final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			writePreamble(writer);
			for(final List<Integer> cl : this.clauses) {
				writeTuple(writer, cl);
			}
			writer.flush();
		}
	}

	private void writePreamble(final BufferedWriter writer) throws IOException {
		writer.write("p cnf ");
		writer.write(Integer.toString(this.nVars));
		writer.write(" ");
		writer.write(Integer.toString(this.clauses.size()));
		writer.write("\n");
	}

	/**
	 * Writes into the specified buffered writer a list of literals, split by space characters, followed by a trailing 0.
	 * 
	 * @param writer the writer
	 * @param tuple the list of literals
	 * @throws IOException if an {@link IOException} occurs while writing the CNF
	 */
	protected void writeTuple(final BufferedWriter writer, final Collection<Integer> tuple) throws IOException {
		final StringBuilder builder = new StringBuilder();
		tuple.stream().forEach(l -> builder.append(l).append(' '));
		builder.append("0\n");
		writer.write(builder.toString());
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

}
