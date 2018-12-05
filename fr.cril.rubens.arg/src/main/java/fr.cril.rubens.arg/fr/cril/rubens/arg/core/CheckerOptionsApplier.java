package fr.cril.rubens.arg.core;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;

/**
 * A class used to apply checker options on AF checkers.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class CheckerOptionsApplier {
	
	private CheckerOptionsApplier() {
		// nothing to do here
	}
	
	/**
	 * Applies options on a checker factory.
	 * 
	 * @param options the options
	 * @param factory the checker factory
	 */
	public static void apply(final String options, final ArgumentationFrameworkCheckerFactory<?> factory) {
		final String[] opts = options.split(",");
		for(final String opt: opts) {
			int indexOf = opt.indexOf('=');
			if(indexOf == -1) {
				continue;
			}
			final String key = opt.substring(0, indexOf);
			if(key.equals("outputFormat")) {
				factory.setOutputFormat(SolverOutputDecoderFactory.getInstanceByName(opt.substring(1+indexOf)).getDecoderInstance());
			}
		}
	}

}
