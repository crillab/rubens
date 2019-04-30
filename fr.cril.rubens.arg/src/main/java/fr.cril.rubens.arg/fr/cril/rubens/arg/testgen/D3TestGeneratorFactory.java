package fr.cril.rubens.arg.testgen;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.D3ArgumentationFramework;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * A test generator for the D3 ("Dung's Triathlon") query.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
@ReflectorParam(name="D3", family="AF")
public class D3TestGeneratorFactory implements TestGeneratorFactory<D3ArgumentationFramework> {

	@Override
	public D3ArgumentationFramework initInstance() {
		return new D3ArgumentationFramework(new ArgumentationFramework());
	}

	@Override
	public List<InstanceTranslator<D3ArgumentationFramework>> translators() {
		final EExtensionSetComputer extComputer = EExtensionSetComputer.COMPLETE_SEM;
		return Stream.of(new InstanceTranslatorAdapter(new NewArgTranslator(extComputer)), new InstanceTranslatorAdapter(new NewAttackTranslator(extComputer))).collect(Collectors.toList());
	}
	
	private class InstanceTranslatorAdapter implements InstanceTranslator<D3ArgumentationFramework> {
		
		private final InstanceTranslator<ArgumentationFramework> adapted;

		private InstanceTranslatorAdapter(final InstanceTranslator<ArgumentationFramework> adaptee) {
			this.adapted = adaptee;
		}

		@Override
		public boolean canBeAppliedTo(final D3ArgumentationFramework instance) {
			return this.adapted.canBeAppliedTo(new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), ExtensionSet.getInstance(Collections.emptySet())));
		}

		@Override
		public D3ArgumentationFramework translate(final D3ArgumentationFramework instance) {
			return new D3ArgumentationFramework(this.adapted.translate(new ArgumentationFramework(instance.getArguments(), instance.getAttacks(), ExtensionSet.getInstance(Collections.emptySet()))));
		}
		
	}

}
