package fr.cril.rubens.checker.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

@ReflectorParam(enabled=false)
public class EchoTestGenerator implements TestGeneratorFactory<EchoInstance> {

	@Override
	public EchoInstance initInstance() {
		return new EchoInstance("");
	}

	@Override
	public List<InstanceTranslator<EchoInstance>> translators() {
		return Stream.of(EchoTranslators.newA(), EchoTranslators.newB()).collect(Collectors.toList());
	}

}
