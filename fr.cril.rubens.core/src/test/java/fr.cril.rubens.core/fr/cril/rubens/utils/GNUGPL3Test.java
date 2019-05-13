package fr.cril.rubens.utils;

import org.junit.Test;
import org.slf4j.LoggerFactory;

public class GNUGPL3Test {
	
	@Test
	public void testWelcomeMessage() {
		GNUGPL3.logWelcomeMessage(LoggerFactory.getLogger(GNUGPL3Test.class));
	}
	
	@Test
	public void testLicense() {
		GNUGPL3.logTermsAndConditions(LoggerFactory.getLogger(GNUGPL3Test.class));
	}

}
