package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.DynamicArgumentationFramework.Translation;
import fr.cril.rubens.arg.testgen.AttackRemovalTranslator;
import fr.cril.rubens.arg.testgen.EExtensionSetComputer;
import fr.cril.rubens.arg.testgen.NewAttackTranslator;
import fr.cril.rubens.arg.utils.Forget;

public class DynamicArgumentationFrameworkTest {
	
	private DynamicArgumentationFramework dynAf;
	private ArgumentationFramework af1;
	private ArgumentationFramework af2;

	@Before
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
	public void testTranslations() {
		final List<Translation> translations = this.dynAf.getTranslations();
		assertEquals(2, translations.size());
		assertEquals(af1, translations.get(0).getArgumentationFramework());
		assertEquals(af2, translations.get(1).getArgumentationFramework());
		assertTrue(translations.get(0).getTranslation().isNewAttack());
		assertFalse(translations.get(1).getTranslation().isNewAttack());
		assertEquals(Stream.of(".apx", ".apxm", ".exts", ".exts.1", ".exts.2").collect(Collectors.toList()), this.dynAf.getFileExtensions());
	}
	
	@Test
	public void testWriteDynamics() throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.dynAf.write(".apxm", os);
		assertEquals("+att(a,a).\n-att(a,a).\n", new String(os.toByteArray()));
	}
	
	@Test
	public void testWriteExts() throws IOException {
		final List<Translation> translations = this.dynAf.getTranslations();
		for(int i=0; i<translations.size(); ++i) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			this.dynAf.write(".exts."+(i+1), os);
			final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
			translations.get(i).getArgumentationFramework().write(".exts", os2);
			assertEquals(new String(os2.toByteArray()), new String(os.toByteArray()));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnexpectedExtension() throws IOException {
		this.dynAf.write(".foo", new ByteArrayOutputStream());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testWriteExtensions() throws IOException {
		this.dynAf.writeExtensions(new ByteArrayOutputStream());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongExtIndex() throws IOException {
		this.dynAf.write(".exts.foo", new ByteArrayOutputStream());
	}
	
	@Test
	public void testWriteInstance() throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		this.dynAf.write(".apx", os);
		final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		this.dynAf.getInitInstance().write(".apx", os2);
		assertEquals(new String(os2.toByteArray()), new String(os.toByteArray()));
	}
	
	@Test
	public void testToString() {
		assertEquals("[[a], [], +att(a,a).,-att(a,a)., [[a]], [[]], [[a]]]", this.dynAf.toString());
	}

}
