package fr.cril.rubens.arg.checking;

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
