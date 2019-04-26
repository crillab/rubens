package fr.cril.rubens.cnf.ddnnf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DDNNFReaderTest {
	
	@Test
	public void testOK() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testTrue() throws DDNNFException {
		final String instance = "nnf 1 0 1\n"
				+ "A 0\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testFalse() throws DDNNFException {
		final String instance = "nnf 1 0 1\n"
				+ "O 0 0";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(0, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testFreeVars() throws DDNNFException {
		final String instance = "nnf 7 6 4\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(8, Utils.countModels(ddnnf));
	}
	
	@Test
	public void testNoConstraints() throws DDNNFException {
		final String instance = "nnf 0 0 1\n"
				+ "\n"
				+ "\n";
		final DDNNFReader reader = new DDNNFReader();
		final DDNNF ddnnf = reader.read(instance);
		assertEquals(2, Utils.countModels(ddnnf));
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongNumberOfVarsInPreamble() throws DDNNFException {
		final String instance = "nnf 7 6 1\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongNumberOfNodesInPreamble() throws DDNNFException {
		final String instance = "nnf 6 6 4\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongNumberOfEdgesInPreamble() throws DDNNFException {
		final String instance = "nnf 7 7 4\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testEmptyString() throws DDNNFException {
		final String instance = "";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testEmptyLine() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testSyntaxErrorInPreamble1() throws DDNNFException {
		final String instance = "nnf 0 0\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testSyntaxErrorInPreamble2() throws DDNNFException {
		final String instance = "dnnf 0 0 1\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testSyntaxErrorInPreamble3() throws DDNNFException {
		final String instance = "nnf a 0 1\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testSyntaxErrorInPreamble4() throws DDNNFException {
		final String instance = "nnf 0 a 1\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testSyntaxErrorInPreamble5() throws DDNNFException {
		final String instance = "nnf 0 0 a\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testUnknownNode() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "AND 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testNoChildrenCountInAnd() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongChildrenCountInAnd() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 3 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testNoChildrenCountInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongChildrenCountInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 3 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testIncompatibleParamsInOr1() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 0\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testIncompatibleParamsInOr2() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 0 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testTooMuchChildrenInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 3 2 5 0\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testNegativeChildrenCount() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A -1 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5 \n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testUnknownCflVarInOr() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 3 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testNoLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testZeroLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L 0\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testWrongLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L 3\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testAlphaLit() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L a\n"
				+ "L 2\n"
				+ "A 2 0 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}
	
	@Test(expected=DDNNFException.class)
	public void testRefToFurtherDeclaredNode() throws DDNNFException {
		final String instance = "nnf 7 6 2\n"
				+ "L -1\n"
				+ "L 2\n"
				+ "A 2 3 1\n"
				+ "L 1\n"
				+ "L -2\n"
				+ "A 2 3 4\n"
				+ "O 1 2 2 5\n";
		final DDNNFReader reader = new DDNNFReader();
		reader.read(instance);
	}

}
