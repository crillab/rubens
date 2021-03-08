package fr.cril.rubens.arg.core;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.core.DynamicArgumentationFramework.Translation;
import fr.cril.rubens.arg.testgen.AttackRemovalTranslator;
import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.arg.utils.Forget;

class DynamicArgumentationFrameworkTest {
	
	private DynamicArgumentationFramework dynAf;
	private ArgumentationFramework af1;
	private ArgumentationFramework af2;

	@BeforeEach
	public void setUp() {
		Forget.all();
		final Argument arg = Argument.getInstance("a");
		this.dynAf = new DynamicArgumentationFramework(new ArgumentationFramework(ArgumentSet.getInstance(Collections.singleton(arg)), AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(arg))))));
		final NewAttackTranslator t1 = new NewAttackTranslator(EExtensionSetComputer.COMPLETE_SEM);
		final ArgumentationFramework initInstance = this.dynAf.getInitInstance();
		final Attack att1 = t1.selectNewAttack(initInstance);
		this.af1 = t1.translate(initInstance, att1);
		this.dynAf.addTranlationNewAttack(att1, af1);
		final AttackRemovalTranslator t2 = new AttackRemovalTranslator(EExtensionSetComputer.COMPLETE_SEM);
		final Attack att2 = t2.selectAttackToRemove(af1);
		this.af2 = t2.translate(af1, att2);
		this.dynAf.addTranslationAttackRemoval(att2, af2);
	}
	
	@Test
	void testTranslations() {
		final List<Translation> translations = this.dynAf.getTranslations();
		assertEquals(2, translations.size());
		assertEquals(af1, translations.get(0).getArgumentationFramework());
		assertEquals(af2, translations.get(1).getArgumentationFramework());
		assertTrue(translations.get(0).getTranslation().isNewAttack());
		assertFalse(translations.get(1).getTranslation().isNewAttack());
		assertEquals(Stream.of(".apx", ".apxm", ".exts", ".exts.1", ".exts.2").collect(Collectors.toList()), this.dynAf.getFileExtensions());
	}
	
	@Test
	void testWriteDynamics() throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.dynAf.write(".apxm", os);
		assertEquals("+att(a,a).\n-att(a,a).\n", new String(os.toByteArray()));
	}
	
	@Test
	void testWriteExts() throws IOException {
		final List<Translation> translations = this.dynAf.getTranslations();
		for(int i=0; i<translations.size(); ++i) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			this.dynAf.write(".exts."+(i+1), os);
			final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
			translations.get(i).getArgumentationFramework().write(".exts", os2);
			assertEquals(new String(os2.toByteArray()), new String(os.toByteArray()));
		}
	}
	
	@Test
	void testUnexpectedExtension() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		assertThrows(IllegalArgumentException.class, () -> this.dynAf.write(".foo", baos));
	}
	
	@Test
	void testWriteExtensions() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		assertThrows(UnsupportedOperationException.class, () -> this.dynAf.writeExtensions(baos));
	}
	
	@Test
	void testWrongExtIndex() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		assertThrows(IllegalArgumentException.class, () -> this.dynAf.write(".exts.foo", baos));
	}
	
	@Test
	void testWriteInstance() throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.dynAf.write(".apx", os);
		final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		this.dynAf.getInitInstance().write(".apx", os2);
		assertEquals(new String(os2.toByteArray()), new String(os.toByteArray()));
	}
	
	@Test
	void testToString() {
		assertEquals("[[a], [], +att(a,a).,-att(a,a)., [[a]], [[]], [[a]]]", this.dynAf.toString());
	}

}
