package fr.cril.rubens.arg.testgen;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;
import fr.cril.rubens.specs.TestGeneratorFactoryParams;

/**
 * A {@link TestGeneratorFactory} instance used to build abstract argumentation problems for the Ideal semantics.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@TestGeneratorFactoryParams(name="ARG-ID")
public class IdealSemTestGeneratorFactory implements TestGeneratorFactory<ArgumentationFramework> {

	@Override
	public ArgumentationFramework initInstance() {
		return new ArgumentationFramework();
	}

	@Override
	public List<InstanceTranslator<ArgumentationFramework>> translators() {
		return Stream.of(new NewArgTranslator(), new NewAttackTranslator(EExtensionSetComputer.IDEAL_SEM)).collect(Collectors.toList());
	}

}
