package fr.cril.rubens.cnf.wmc;

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
import java.math.BigInteger;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.cnf.wmodels.WeightedModelsCnfInstance;
import fr.cril.rubens.specs.Instance;

/**
 * An {@link Instance} implementation dedicated to the weighted model counting problem for CNF formulas.
 * 
 * This class is built on top of the {@link WeightedModelsCnfInstance}, and mainly translates the set of weighted models into the sum of their weights.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class WeightedModelCountingCnfInstance extends WeightedModelsCnfInstance {

	/** the default extension for weighted model counts */
	public static final String WMC_EXT = ".wmc";

	/**
	 * Builds a new empty instance.
	 */
	public WeightedModelCountingCnfInstance() {
		super();
	}

	/**
	 * The copy constructor of a {@link CnfInstance}.
	 * 
	 * @param instance the instance to copy
	 */
	public WeightedModelCountingCnfInstance(final CnfInstance instance) {
		super(instance);
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, W_EXT, WMC_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			super.writeCNF(os);
		} else if(W_EXT.equals(extension)) {
			super.writeWeights(os);
		} else if(WMC_EXT.equals(extension)) {
			os.write(super.modelWeights().values().stream().reduce(BigInteger.ZERO, (a,b) -> a.add(b)).toString().getBytes());
			os.write('\n');
		} else {
			throw new IllegalArgumentException("unknown extension "+extension);
		}
	}

}
