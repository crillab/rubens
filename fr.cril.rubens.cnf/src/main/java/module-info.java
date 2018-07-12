/**
 * @author Emmanuel Lonca - lonca@cril.fr
 */
module fr.cril.rubens.cnf {
	
	exports fr.cril.rubens.cnf.core;
	exports fr.cril.rubens.cnf.mc;
	exports fr.cril.rubens.cnf.wmodels;
	exports fr.cril.rubens.cnf.wmc;
	
	requires fr.cril.rubens.core;

	requires org.ow2.sat4j.core;
	requires org.slf4j;
	
}
