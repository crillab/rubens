package fr.cril.rubens.cnf.utils;

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

import fr.cril.rubens.cnf.core.CnfInstance;

/**
 * A class providing utilities to write CNF-related contents into output streams.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class WriteUtils {
	
	private WriteUtils() {
		// nothing to do here; just hiding the default constructor
	}
	
	/**
	 * Writes a CNF into the specified output stream.
	 * 
	 * The CNF is written using the DIMACS format.
	 * 
	 * @param instance the CNF instance
	 * @param os the output stream
	 * @throws IOException if an {@link IOException} occurs while writing the CNF
	 */
	public static final void writeCNF(final CnfInstance instance, final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			writePreamble(instance, writer);
			for(final List<Integer> cl : instance.clauses()) {
				writeTuple(writer, cl);
			}
			writer.flush();
		}
	}
	
	private static final void writePreamble(final CnfInstance instance, final BufferedWriter writer) throws IOException {
		writer.write("p cnf ");
		writer.write(Integer.toString(instance.nVars()));
		writer.write(" ");
		writer.write(Integer.toString(instance.clauses().size()));
		writer.write("\n");
	}
	
	/**
	 * Writes a list of models into a stream.
	 * 
	 * One model is written per line.
	 * Each model is written using the same format as the one used by SAT solvers. 
	 * 
	 * @param models the models
	 * @param os the output stream
	 * @throws IOException if an I/O error occurs while writing into the stream
	 */
	public static final void writeModels(final List<List<Integer>> models, final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			for(final List<Integer> cl : models) {
				writeTuple(writer, cl);
			}
			writer.flush();
		}
	}
	
	/**
	 * Writes into the specified buffered writer a list of literals, split by space characters, followed by a trailing 0.
	 * 
	 * @param writer the writer
	 * @param tuple the list of literals
	 * @throws IOException if an {@link IOException} occurs while writing the CNF
	 */
	public static final void writeTuple(final BufferedWriter writer, final Collection<Integer> tuple) throws IOException {
		final StringBuilder builder = new StringBuilder();
		tuple.stream().forEach(l -> builder.append(l).append(' '));
		builder.append("0\n");
		writer.write(builder.toString());
		writer.flush();
	}

}
