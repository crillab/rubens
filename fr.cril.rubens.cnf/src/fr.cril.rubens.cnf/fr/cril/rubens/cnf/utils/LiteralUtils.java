package fr.cril.rubens.cnf.utils;

import java.util.List;
import java.util.Random;

/**
 * Utility class for literals handling.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class LiteralUtils {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	private LiteralUtils() {
		// hiding the public constructor
	}
	
	/**
	 * Select a random literal in the candidates list.
	 * 
	 * The literals set to 0 are ignored.
	 * No check is performed; it is assumed there is at least one non-null literal.
	 * 
	 * @param litCandidates the literal candidates
	 * @return the selected literal
	 */
	public static int selectRandomLiteral(final List<Integer> litCandidates) {
		int lit = 0;
		do {
			final int index = RANDOM.nextInt(litCandidates.size());
			final int curLit = litCandidates.get(index);
			if(curLit == 0) {
				litCandidates.set(index, litCandidates.get(litCandidates.size()-1));
				litCandidates.remove(litCandidates.size()-1);
			} else {
				lit = curLit;
			}
		} while(lit == 0);
		return lit;
	}
	
	/**
	 * A static method used to translate literals form the signed integer format to the unsigned format used in most SAT solvers.
	 * 
	 * In this output format, the sign is given by the least significant bit, while the variable number is given by the other bits.
	 * 
	 * @param lit the signed-formatted literal
	 * @return the unsigned-formatted literal
	 */
	public static int dimacsToInternal(final int lit) {
		final int abs = lit > 0 ? lit : -lit;
		return ((abs-1) << 1) + (lit < 0 ? 1 : 0);
	}

}
