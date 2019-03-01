package fr.cril.rubens.arg.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.arg.checking.decoders.SolverOutputDecoderFactory;

/**
 * A class used to apply checker options on AF checkers.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public final class CheckerOptionsApplier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckerOptionsApplier.class);
	
	private CheckerOptionsApplier() {
		// nothing to do here
	}
	
	/**
	 * Applies options on a checker factory.
	 * 
	 * Returns <code>true</code> iff all options were recognized.
	 * 
	 * @param options the options
	 * @param factory the checker factory
	 * @return <code>true</code> iff all options were recognized
	 */
	public static boolean apply(final String options, final ArgumentationFrameworkCheckerFactory<?> factory) {
		boolean allOk = true;
		final String[] opts = options.split(",");
		for(final String opt: opts) {
			if(opt.trim().isEmpty()) {
				continue;
			}
			int indexOf = opt.indexOf('=');
			if(indexOf != -1 && opt.substring(0, indexOf).equals("outputFormat")) {
				factory.setOutputFormat(SolverOutputDecoderFactory.getInstanceByName(opt.substring(1+indexOf)).getDecoderInstance());
			} else {
				LOGGER.warn("invalid checker option: {}", opt);
				allOk = false;
			}
		}
		return allOk;
	}

}
