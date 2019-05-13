package fr.cril.rubens.arg.checking.decoders;

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

import java.util.function.Supplier;

/**
 * A factory class for solver output decoders.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public enum SolverOutputDecoderFactory {
	
	/** ICCMA2017 decoder */
	ICCMA17("ICCMA17", ICCMA17SolverOutputDecoder::new),
	
	/** ICCMA2017 decoder */
	ICCMA19("ICCMA19", ICCMA19SolverOutputDecoder::new);
	
	/** the name of the format */
	private final String name;
	
	/** a supplier for format decoder instances */
	private final Supplier<ISolverOutputDecoder> decoderSupplier;

	private SolverOutputDecoderFactory(final String name, final Supplier<ISolverOutputDecoder> decoderSupplier) {
		this.name = name;
		this.decoderSupplier = decoderSupplier;
	}
	
	/**
	 * Get an instance of the decoder.
	 * 
	 * @return an instance of the decoder
	 */
	public ISolverOutputDecoder getDecoderInstance() {
		return this.decoderSupplier.get();
	}
	
	/**
	 * Gets an instance corresponding to the provided format name.
	 * 
	 * @param name the format name
	 * @return the corresponding instance
	 * @throws IllegalArgumentException if no format matches the provided name
	 */
	public static SolverOutputDecoderFactory getInstanceByName(final String name) {
		for(final SolverOutputDecoderFactory decoder : SolverOutputDecoderFactory.values()) {
			if(decoder.name.equals(name)) {
				return decoder;
			}
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Returns the default decoder.
	 * 
	 * The default decoder is currently ICCMA2019.
	 * 
	 * @return the default decoder
	 */
	public static SolverOutputDecoderFactory getDefault() {
		return SolverOutputDecoderFactory.ICCMA19;
	}

}
