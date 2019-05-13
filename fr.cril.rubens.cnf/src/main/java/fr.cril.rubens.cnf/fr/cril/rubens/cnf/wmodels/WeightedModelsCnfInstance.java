package fr.cril.rubens.cnf.wmodels;

/*-
 * #%L
 * RUBENS module for CNF handling
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.core.CnfInstance;

/**
 * An extension of the CNF instance where models are weighted.
 * 
 * Weights are given by integers and are associated to literals.
 * The default weight for a literal is 1.
 * The weight of a model is the product of the weights of the literals it contains.
 * 
 * Weights are stored by text lines composed of two integers, where the first one is the literal in the signed form and the second one is the weight.
 * 
 * Weighted models are stored just like regular models, except the weight is added to the beginning of the model line.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class WeightedModelsCnfInstance extends CnfInstance {

	/** the default extension for weight files */
	public static final String W_EXT = ".w";

	/** the default extension for weighted models files */
	public static final String WMODS_EXT = ".wmods";

	private static final Random RANDOM = new Random(0);

	private final List<Integer> objWeightsPositiveLits;

	private final List<Integer> objWeightsNegativeLits;

	private final Map<Set<Integer>, BigInteger> modelWeights;

	/**
	 * Build a new empty instance.
	 */
	public WeightedModelsCnfInstance() {
		super();
		this.objWeightsPositiveLits = Collections.emptyList();
		this.objWeightsNegativeLits = Collections.emptyList();
		this.modelWeights = new HashMap<>();
		this.modelWeights.put(Collections.emptySet(), BigInteger.ONE);
	}

	/**
	 * The copy constructor of a {@link CnfInstance}.
	 * 
	 * @param cnfInstance the instance to copy
	 */
	public WeightedModelsCnfInstance(final CnfInstance cnfInstance) {
		super(cnfInstance);
		this.objWeightsPositiveLits = randomObjectiveWeights();
		this.objWeightsNegativeLits = randomObjectiveWeights();
		final Set<Set<Integer>> models = super.models();
		this.modelWeights = new HashMap<>();
		models.stream().forEach(m -> this.modelWeights.put(m, modelWeight(m)));
	}

	private List<Integer> randomObjectiveWeights() {
		final int nVars = super.nVars();
		final List<Integer>  weights = new ArrayList<>(nVars);
		for(int i=0; i<nVars; ++i) {
			weights.add(RANDOM.nextInt(1+nVars) * ((RANDOM.nextInt()&1) == 0 ? -1 : 1));
		}
		return weights;
	}

	private BigInteger modelWeight(final Set<Integer> model) {
		BigInteger weight = BigInteger.ONE;
		for(final Integer lit : model) {
			final int intWeight = lit > 0 ? this.objWeightsPositiveLits.get(lit-1) : this.objWeightsNegativeLits.get(-lit-1);
			weight = weight.multiply(BigInteger.valueOf(intWeight));
		}
		return weight;
	}

	/**
	 * Returns the weights associated to the models.
	 * 
	 * @return the weights associated to the models
	 */
	public Map<Set<Integer>, BigInteger> modelWeights() {
		return Collections.unmodifiableMap(this.modelWeights);
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, W_EXT, WMODS_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			super.writeCNF(os);
		} else if(W_EXT.equals(extension)) {
			writeWeights(os);
		} else if(WMODS_EXT.equals(extension)) {
			writeWeightedModels(os);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Writes the weights to the provided output stream.
	 * 
	 * Weights are stored by text lines composed of two integers, where the first one is the literal in the signed form and the second one is the weight.
	 * 
	 * @param os the output stream
	 * @throws IOException if an I/O error occurs while writing the weights
	 */
	protected void writeWeights(final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			for(int i=0; i<this.objWeightsPositiveLits.size(); ++i) {
				final int weight = this.objWeightsPositiveLits.get(i);
				if(weight != 1) {
					writer.write(Integer.toString(i+1));
					writer.write(' ');
					writer.write(Integer.toString(weight));
					writer.write('\n');
				}
			}
			for(int i=0; i<this.objWeightsNegativeLits.size(); ++i) {
				final int weight = this.objWeightsNegativeLits.get(i);
				if(weight != 1) {
					writer.write(Integer.toString(-i-1));
					writer.write(' ');
					writer.write(Integer.toString(weight));
					writer.write('\n');
				}
			}
		}
	}

	private void writeWeightedModels(final OutputStream os) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			Set<Set<Integer>> models = super.models();
			for(final Set<Integer> model : models) {
				writer.write(this.modelWeights.get(model).toString());
				writer.write(' ');
				writeTuple(writer, model);
			}
			writer.flush();
		}
	}

}
