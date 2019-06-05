package fr.cril.rubens.cnf.mc;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.utils.WriteUtils;
import fr.cril.rubens.specs.Instance;

/**
 * An {@link Instance} implementation dedicated to the model counting problem for CNF formulas.
 * 
 * This class is built on top of the {@link CnfInstance}, and mainly translates the set of models into its size.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ModelCountingCnfInstance extends CnfInstance {
	
	/** the default extension for files storing the model count */
	public static final String MC_EXT = ".mc";
	
	/**
	 * Builds a new model counting instance.
	 */
	public ModelCountingCnfInstance() {
		super();
	}
	
	/**
	 * The copy constructor of a {@link CnfInstance}.
	 * 
	 * @param instance the instance to copy
	 */
	public ModelCountingCnfInstance(final CnfInstance instance) {
		super(instance);
	}
	
	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, MC_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			WriteUtils.writeCNF(this, os);
		} else if(MC_EXT.equals(extension)) {
			os.write(Integer.toString(super.models().size()).getBytes());
			os.write('\n');
		} else {
			throw new IllegalArgumentException("unknown extension "+extension);
		}
	}
	
}
