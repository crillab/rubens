package fr.cril.rubens.cnf.utils;

/*-
 * #%L
 * RUBENS
 * %%
 * Copyright (C) 2019 Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   *   CRIL - initial API and implementation
 * #L%
 */

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.cnf.core.CnfInstance;

public class WriteUtilsTest {
	
	private CnfInstance cnf;
	
	@Before
	public void setUp() {
		final List<List<Integer>> ll1 = Collections.singletonList(Collections.singletonList(1));
		this.cnf = new CnfInstance(1, ll1, ll1);
	}
	
	@Test
	public void testWriteCnf() throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		WriteUtils.writeCNF(this.cnf, out);
		assertEquals("p cnf 1 1\n1 0\n", out.toString());
	}
	
	@Test
	public void testWriteModels() throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		WriteUtils.writeModels(this.cnf.models(), out);
		assertEquals("1 0\n", out.toString());
	}
	
	@Test
	public void testWriteTuple() throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		WriteUtils.writeTuple(new BufferedWriter(new OutputStreamWriter(out)), this.cnf.clauses().get(0));
		assertEquals("1 0\n", out.toString());
	}

}
