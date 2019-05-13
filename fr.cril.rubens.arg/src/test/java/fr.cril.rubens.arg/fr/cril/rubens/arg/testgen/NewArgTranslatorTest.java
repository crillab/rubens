package fr.cril.rubens.arg.testgen;

/*-
 * #%L
 * RUBENS module for abstract argumention problems
 * %%
 * Copyright (C) 2019 Centre de Recherche en Informatique de Lens (CRIL) — Artois University and CNRS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.AttackSet;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.arg.testgen.NewArgTranslator;
import fr.cril.rubens.arg.utils.Forget;

public class NewArgTranslatorTest {
	
	private NewArgTranslator translator;
	
	private ArgumentationFramework af;

	private Set<Attack> atts;

	private Set<Set<Argument>> exts;

	@Before
	public void setUp() {
		Forget.all();
		this.translator = new NewArgTranslator(EExtensionSetComputer.COMPLETE_SEM);
		final Argument argA = Argument.getInstance("a0");
		final Argument argB = Argument.getInstance("a1");
		final Set<Argument> args = Stream.of(argA, argB).collect(Collectors.toSet());
		this.atts = Stream.of(Attack.getInstance(argA, argB)).collect(Collectors.toSet());
		this.exts = Stream.of(Stream.of(argA).collect(Collectors.toSet())).collect(Collectors.toSet());
		this.af = new ArgumentationFramework(ArgumentSet.getInstance(args), AttackSet.getInstance(atts), exts.stream().map(ArgumentSet::getInstance).collect(ExtensionSet.collector()));
	}
	
	@Test
	public void testCanBeAppliedEmptyInstance() {
		final ArgumentationFramework af = new ArgumentationFramework();
		assertTrue(this.translator.canBeAppliedTo(af));
	}
	
	@Test
	public void testTranslateEmptyInstance() {
		final ArgumentationFramework af = new ArgumentationFramework();
		final ArgumentationFramework newAf = this.translator.translate(af);
		final Argument arg = Argument.getInstance("a0");
		assertEquals(Stream.of(arg).collect(ArgumentSet.collector()), newAf.getArguments());
		assertEquals(AttackSet.getInstance(Collections.emptySet()), newAf.getAttacks());
		assertEquals(Stream.of(Stream.of(arg).collect(ArgumentSet.collector())).collect(ExtensionSet.collector()), newAf.getExtensions());
	}
	
	@Test
	public void testCanBeApplied() {
		assertTrue(this.translator.canBeAppliedTo(this.af));
	}
	
	@Test
	public void testTranslate() {
		final ArgumentationFramework newAf = this.translator.translate(this.af);
		final Argument newArg = Argument.getInstance("a2");
		assertEquals(Stream.of(Argument.getInstance("a0"), Argument.getInstance("a1"), newArg).collect(ArgumentSet.collector()), newAf.getArguments());
		assertEquals(AttackSet.getInstance(this.atts), newAf.getAttacks());
		assertEquals(Stream.of(Stream.concat(this.exts.iterator().next().stream(), Stream.of(newArg)).collect(ArgumentSet.collector())).collect(ExtensionSet.collector()), newAf.getExtensions());
	}

}
