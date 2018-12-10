package fr.cril.rubens.arg.checking.decoders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolverOutputDecoderFactoryTest {
	
	@Test
	public void testGetICCMA17ByName() {
		assertEquals(ICCMA17SolverOutputDecoder.class, SolverOutputDecoderFactory.getInstanceByName("ICCMA17").getDecoderInstance().getClass());
	}
	
	@Test
	public void testGetICCMA19ByName() {
		assertEquals(ICCMA19SolverOutputDecoder.class, SolverOutputDecoderFactory.getInstanceByName("ICCMA19").getDecoderInstance().getClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetByWrongName() {
		SolverOutputDecoderFactory.getInstanceByName("Foo");
	}

}
