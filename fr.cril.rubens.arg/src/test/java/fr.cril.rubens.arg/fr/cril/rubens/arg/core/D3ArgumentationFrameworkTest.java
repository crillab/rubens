package fr.cril.rubens.arg.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import fr.cril.rubens.arg.utils.Forget;

public class D3ArgumentationFrameworkTest {
	
	@Before
	public void setUp() {
		Forget.all();
	}
	
	@Test
	public void testExtensions() {
		final List<Argument> argl = IntStream.range(0, 4).mapToObj(i -> "a"+i).map(Argument::getInstance).collect(Collectors.toList());
		final ArgumentSet args = argl.stream().collect(ArgumentSet.collector());
		final AttackSet attacks = AttackSet.getInstance(Stream.of(
				Attack.getInstance(argl.get(3), argl.get(0)),
				Attack.getInstance(argl.get(0), argl.get(1)),
				Attack.getInstance(argl.get(0), argl.get(2)),
				Attack.getInstance(argl.get(1), argl.get(2)),
				Attack.getInstance(argl.get(2), argl.get(1))
		).collect(Collectors.toSet()));
		final D3ArgumentationFramework d3af = new D3ArgumentationFramework(new ArgumentationFramework(args, attacks, ExtensionSet.getInstance(Collections.emptySet())));
		assertEquals(ExtensionSet.getInstance(Collections.singleton(ArgumentSet.getInstance(Collections.singleton(argl.get(3))))), d3af.getGrExts());
		final ExtensionSet bothExts = Stream.of(
				Stream.of(argl.get(3), argl.get(1)).collect(ArgumentSet.collector()),
				Stream.of(argl.get(3), argl.get(2)).collect(ArgumentSet.collector())
		).collect(ExtensionSet.collector());
		assertEquals(bothExts, d3af.getStExts());
		assertEquals(bothExts, d3af.getPrExts());
	}
	
	@Test
	public void testToString() {
		final List<Argument> argl = IntStream.range(0, 4).mapToObj(i -> "a"+i).map(Argument::getInstance).collect(Collectors.toList());
		final ArgumentSet args = argl.stream().collect(ArgumentSet.collector());
		final AttackSet attacks = AttackSet.getInstance(Stream.of(
				Attack.getInstance(argl.get(3), argl.get(0)),
				Attack.getInstance(argl.get(0), argl.get(1)),
				Attack.getInstance(argl.get(0), argl.get(2)),
				Attack.getInstance(argl.get(1), argl.get(2)),
				Attack.getInstance(argl.get(2), argl.get(1))
		).collect(Collectors.toSet()));
		final D3ArgumentationFramework d3af = new D3ArgumentationFramework(new ArgumentationFramework(args, attacks, ExtensionSet.getInstance(Collections.emptySet())));
		assertEquals("[[a0, a1, a2, a3], [att(a0,a1), att(a0,a2), att(a1,a2), att(a2,a1), att(a3,a0)], [[a3]], [[a1, a3], [a2, a3]], [[a1, a3], [a2, a3]]]", d3af.toString());
	}
	
	@Test
	public void testWriteExtensions() throws IOException {
		final List<Argument> argl = IntStream.range(0, 4).mapToObj(i -> "a"+i).map(Argument::getInstance).collect(Collectors.toList());
		final ArgumentSet args = argl.stream().collect(ArgumentSet.collector());
		final AttackSet attacks = AttackSet.getInstance(Stream.of(
				Attack.getInstance(argl.get(3), argl.get(0)),
				Attack.getInstance(argl.get(0), argl.get(1)),
				Attack.getInstance(argl.get(0), argl.get(2)),
				Attack.getInstance(argl.get(1), argl.get(2)),
				Attack.getInstance(argl.get(2), argl.get(1))
		).collect(Collectors.toSet()));
		final D3ArgumentationFramework d3af = new D3ArgumentationFramework(new ArgumentationFramework(args, attacks, ExtensionSet.getInstance(Collections.emptySet())));
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		d3af.writeExtensions(os);
		assertEquals("[[a3]],[[a1,a3],[a2,a3]],[[a1,a3],[a2,a3]]\n", new String(os.toByteArray()));
	}

}
