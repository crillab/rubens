/**
 * Core types for the RUBENS platform.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
module fr.cril.rubens.core {
	
	exports fr.cril.rubens.core;
	exports fr.cril.rubens.options;
	exports fr.cril.rubens.reflection;
	exports fr.cril.rubens.specs;
	exports fr.cril.rubens.utils;

	requires io.github.lukehutch.fastclasspathscanner;
	requires org.slf4j;
	requires commons.cli;
	
}
