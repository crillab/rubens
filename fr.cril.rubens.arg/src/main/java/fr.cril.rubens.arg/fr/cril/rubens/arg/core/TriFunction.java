package fr.cril.rubens.arg.core;

/**
 * A function taking three parameters and returning a result.
 *  
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <A> the type of the first parameter
 * @param <B> the type of the second parameter
 * @param <C> the type of the third parameter
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface TriFunction<A,B,C,R> {
	
	/**
	 * Applies the function.
	 * 
	 * @param a the first parameter
	 * @param b the second parameter
	 * @param c the third parameter
	 * @return the result
	 */
	R apply(A a, B b, C c);

}
