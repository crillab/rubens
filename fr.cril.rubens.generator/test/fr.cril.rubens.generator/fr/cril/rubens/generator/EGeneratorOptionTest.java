package fr.cril.rubens.generator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class EGeneratorOptionTest {
	
	@Test
	public void testOptionsNames() {
		final Set<String> genOptNames = Arrays.stream(EGeneratorOption.values()).map(EGeneratorOption::getOpt).collect(Collectors.toSet());
		final Set<String> apacheOptNames = EGeneratorOption.buildCliOptions().getOptions().stream().map(Option::getOpt).collect(Collectors.toSet());
		assertEquals(apacheOptNames, genOptNames);
	}
	
	@Test
	public void testNoNullConsumer() {
		assertTrue(Arrays.stream(EGeneratorOption.values()).map(EGeneratorOption::getOptionConsumer).noneMatch(Objects::isNull));
	}

}
