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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.cril.rubens.arg.utils.Forget;

class ArgumentationFrameworkTest {
	
	@BeforeEach
	public void setUp() {
		Forget.all();
	}
	
	@Test
	void testEmptyAF() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertEquals(ArgumentSet.getInstance(Collections.emptySet()), af.getArguments());
		assertEquals(AttackSet.getInstance(Collections.emptySet()), af.getAttacks());
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))), af.getExtensions());
	}
	
	@Test
	void testAf() {
		final Argument argA = Argument.getInstance("a");
		final Argument argB = Argument.getInstance("b");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		final Set<Attack> atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		final Set<Set<Argument>> exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		final ArgumentationFramework af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
		assertEquals(ArgumentSet.getInstance(args), af.getArguments());
		assertEquals(AttackSet.getInstance(atts), af.getAttacks());
		assertEquals(exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()), af.getExtensions());
	}
	
	@Test
	void testFileExtensions() {
		final Collection<String> fileExtensions = new ArgumentationFramework().getFileExtensions();
		assertEquals(2, fileExtensions.size());
		assertTrue(fileExtensions.contains(".apx"));
		assertTrue(fileExtensions.contains(".exts"));
	}
	
	@Test
	void testWriteInstanceEmptyAf() throws IOException {
		final ArgumentationFramework af = new ArgumentationFramework();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		af.write(".apx", os);
		final List<String> lines = Arrays.stream(new String(os.toByteArray()).split("\n")).map(String::trim).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		assertEquals(0, lines.size());
	}
	
	@Test
	void testWriteExtensionEmptyAf() throws IOException {
		final ArgumentationFramework af = new ArgumentationFramework();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		af.write(".exts", os);
		final List<String> lines = Arrays.stream(new String(os.toByteArray()).split("\n")).map(String::trim).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		assertEquals(3, lines.size());
		assertEquals("[", lines.get(0));
		assertEquals("[]", lines.get(1));
		assertEquals("]", lines.get(2));
	}
	
	@Test
	void testWriteInstance() throws IOException {
		final Argument argA = Argument.getInstance("a");
		final Argument argB = Argument.getInstance("b");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		final Set<Attack> atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		final Set<Set<Argument>> exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		final ArgumentationFramework af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		af.write(".apx", os);
		final List<String> lines = Arrays.stream(new String(os.toByteArray()).split("\n")).map(String::trim).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		assertEquals(3, lines.size());
		assertTrue(lines.contains("arg(a)."));
		assertTrue(lines.contains("arg(b)."));
		assertTrue(lines.contains("att(a,b)."));
	}
	
	@Test
	void testWriteExtension() throws IOException {
		final Argument argA = Argument.getInstance("a");
		final Argument argB = Argument.getInstance("b");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		final Set<Attack> atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		final Set<Set<Argument>> exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		final ArgumentationFramework af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		af.write(".exts", os);
		final List<String> lines = Arrays.stream(new String(os.toByteArray()).split("\n")).map(String::trim).filter(l -> !l.isEmpty()).collect(Collectors.toList());
		assertEquals(3, lines.size());
		assertEquals("[", lines.get(0));
		assertEquals("[a]", lines.get(1));
		assertEquals("]", lines.get(2));
	}
	
	@Test
	void testNullArgs() {
		final ArgumentSet argSet = null;
		final AttackSet attSet = AttackSet.getInstance(Collections.emptySet());
		final ExtensionSet extSet = ExtensionSet.getInstance(Collections.emptySet());
		assertThrows(IllegalArgumentException.class, () -> new ArgumentationFramework(argSet, attSet, extSet));
	}
	
	@Test
	void testNullAttacks() {
		final ArgumentSet argSet = ArgumentSet.getInstance(Collections.emptySet());
		final AttackSet attSet = null;
		final ExtensionSet extSet = ExtensionSet.getInstance(Collections.emptySet());
		assertThrows(IllegalArgumentException.class, () -> new ArgumentationFramework(argSet, attSet, extSet));
	}
	
	@Test
	void testNullExtSet() {
		final ArgumentSet argSet = ArgumentSet.getInstance(Collections.emptySet());
		final AttackSet attSet = AttackSet.getInstance(Collections.emptySet());
		final ExtensionSet extSet = null;
		assertThrows(IllegalArgumentException.class, () -> new ArgumentationFramework(argSet, attSet, extSet));
	}
	
	@Test
	void testHashcode() {
		final ArgumentationFramework af = new ArgumentationFramework(
				ArgumentSet.getInstance(Collections.emptySet()),
				AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))));
		assertEquals(new ArgumentationFramework().hashCode(), af.hashCode());
	}
	
	@Test
	void testEquals() {
		final ArgumentationFramework af = new ArgumentationFramework(
				ArgumentSet.getInstance(Collections.emptySet()),
				AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.emptySet()))));
		assertEquals(new ArgumentationFramework(), af);
	}
	
	@Test
	void testEquals2() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertEquals(af, af);
	}
	
	@Test
	void testNotEqualsNull() {
		assertNotEquals(null, new ArgumentationFramework());
	}
	
	@Test
	void testNotEqualsNotAF() {
		assertNotEquals(new ArgumentationFramework(), new Object());
	}
	
	@Test
	void testNotEqualsArgs() {
		assertNotEquals(new ArgumentationFramework(), new ArgumentationFramework(
				ArgumentSet.getInstance(Collections.singleton(Argument.getInstance("a"))),
				AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	void testNotEqualsExtAtts() {
		assertNotEquals(new ArgumentationFramework(), new ArgumentationFramework(
				ArgumentSet.getInstance(Collections.emptySet()),
				AttackSet.getInstance(Collections.singleton(Attack.getInstance(Argument.getInstance("a1"), Argument.getInstance("a2")))),
				ExtensionSet.getInstance(Collections.emptySet())));
	}
	
	@Test
	void testNotEqualsExtSets() {
		assertNotEquals(new ArgumentationFramework(), new ArgumentationFramework(
				ArgumentSet.getInstance(Collections.emptySet()),
				AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(Argument.getInstance("a")))))));
	}
	
	@Test
	void testWriteNullOS() throws IOException {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertThrows(IllegalArgumentException.class, () -> af.write(".apx", null));
	}
	
	@Test
	void testWrongExt() throws IOException {
		final ArgumentationFramework af = new ArgumentationFramework();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		assertThrows(IllegalArgumentException.class, () -> af.write(".foo", baos));
	}
	
	@Test
	void testToString() {
		assertEquals("[[], [], [[]]]", new ArgumentationFramework().toString());
	}
	
	@Test
	void testNullArgUnderDecision() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertThrows(IllegalArgumentException.class, () -> af.setArgUnderDecision(null));
	}
	
	@Test
	void testUnknownArgUnderDecision() {
		final ArgumentationFramework af = new ArgumentationFramework();
		final Argument arg = Argument.getInstance("a");
		assertThrows(IllegalArgumentException.class, () -> af.setArgUnderDecision(arg));
	}
	
	@Test
	void testArgUnderDecision() {
		final Argument argA = Argument.getInstance("a");
		final Argument argB = Argument.getInstance("b");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		final Set<Attack> atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		final Set<Set<Argument>> exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		final ArgumentationFramework af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
		af.setArgUnderDecision(argA);
		assertEquals(argA, af.getArgUnderDecision());
	}
	
	@Test
	void testWriteNoExtensions() throws IOException {
		final ArgumentationFramework af = new ArgumentationFramework(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()),
				ExtensionSet.getInstance(Collections.emptySet()));
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		af.writeExtensions(os);
		assertEquals("[]", new String(os.toByteArray()).replaceAll("\n", ""));
	}

}
