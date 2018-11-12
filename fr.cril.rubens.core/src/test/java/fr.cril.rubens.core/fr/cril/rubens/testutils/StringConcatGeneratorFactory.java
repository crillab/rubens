package fr.cril.rubens.testutils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.ReflectorParam;
import fr.cril.rubens.specs.TestGeneratorFactory;

@ReflectorParam(enabled=false)
public class StringConcatGeneratorFactory implements TestGeneratorFactory<StringInstance> {

	@Override
	public StringInstance initInstance() {
		return new StringInstance("");
	}

	@Override
	public List<InstanceTranslator<StringInstance>> translators() {
		return Stream.of(new StringConcatTranslator("1"), new StringConcatTranslator("2")).collect(Collectors.toList());
	}

}