package fr.cril.rubens.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class ECheckerOptionTest {
	
	@Test
	public void testOptionsNames() {
		final Set<String> genOptNames = Arrays.stream(ECheckerOption.values()).map(ECheckerOption::getOpt).collect(Collectors.toSet());
		final Set<String> apacheOptNames = ECheckerOption.buildCliOptions().getOptions().stream().map(Option::getOpt).collect(Collectors.toSet());
		assertEquals(apacheOptNames, genOptNames);
	}
	
	@Test
	public void testNoNullConsumer() {
		assertTrue(Arrays.stream(ECheckerOption.values()).map(ECheckerOption::getOptionConsumer).noneMatch(Objects::isNull));
	}

}
