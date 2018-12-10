package fr.cril.rubens.arg.testgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.Attack;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;
import fr.cril.rubens.reflection.ReflectorParam;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

@ReflectorParam(enabled=false)
/**
 * A decorator for test generators used to produce dynamic semantics test generators from their "static" counterparts.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class DynamicSemTestGeneratorFactoryDecorator implements TestGeneratorFactory<DynamicArgumentationFramework> {
	
	private TestGeneratorFactory<ArgumentationFramework> decorated;

	private final NewAttackTranslator newAttackTranslator;
	
	private final AttackRemovalTranslator attackRemovalTranslator;
	
	private static final Random RND = new Random();

	/**
	 * Builds a decorator given the decorated element and an extension set computer for the semantics under consideration.
	 * 
	 * @param decorated the decorated test factory
	 * @param extensionSetComputer the extension set computer
	 */
	public DynamicSemTestGeneratorFactoryDecorator(final TestGeneratorFactory<ArgumentationFramework> decorated, final EExtensionSetComputer extensionSetComputer) {
		this.decorated = decorated;
		this.newAttackTranslator = new NewAttackTranslator(extensionSetComputer);
		this.attackRemovalTranslator = new AttackRemovalTranslator(extensionSetComputer);
	}

	@Override
	public DynamicArgumentationFramework initInstance() {
		return new DynamicArgumentationFramework(this.decorated.initInstance());
	}

	@Override
	public List<InstanceTranslator<DynamicArgumentationFramework>> translators() {
		final List<InstanceTranslator<DynamicArgumentationFramework>> translators = new ArrayList<>();
		for(final InstanceTranslator<ArgumentationFramework> translator : this.decorated.translators()) {
			translators.add(new InstanceTranslatorAdapter(translator));
		}
		return Collections.unmodifiableList(translators);
	}
	
	private class InstanceTranslatorAdapter implements InstanceTranslator<DynamicArgumentationFramework> {
		
		private final InstanceTranslator<ArgumentationFramework> adaptee;
		
		private InstanceTranslatorAdapter(final InstanceTranslator<ArgumentationFramework> adaptee) {
			this.adaptee = adaptee;
		}

		@Override
		public boolean canBeAppliedTo(final DynamicArgumentationFramework instance) {
			return this.adaptee.canBeAppliedTo(instance.getInitInstance());
		}

		@Override
		public DynamicArgumentationFramework translate(final DynamicArgumentationFramework instance) {
			final DynamicArgumentationFramework dynInstance = new DynamicArgumentationFramework(this.adaptee.translate(instance.getInitInstance()));
			addDynamics(dynInstance);
			return dynInstance;
		}

		private void addDynamics(final DynamicArgumentationFramework af) {
			final int nDyn = 1 + (int) Math.floor(Math.log(1.+af.getInitInstance().getArguments().size()));
			ArgumentationFramework lastAf = af.getInitInstance();
			for(int i=0; i<nDyn; ++i) {
				lastAf = addDynamics(af, lastAf);
			}
		}

		private ArgumentationFramework addDynamics(final DynamicArgumentationFramework dynAf, ArgumentationFramework lastAf) {
			if(RND.nextBoolean()) {
				if(newAttackTranslator.canBeAppliedTo(lastAf)) {
					lastAf = addAttackDyn(dynAf, lastAf);
				} else {
					lastAf = removeAttackDyn(dynAf, lastAf);
				}
			} else {
				if(attackRemovalTranslator.canBeAppliedTo(lastAf)) {
					lastAf = removeAttackDyn(dynAf, lastAf);
				} else {
					lastAf = addAttackDyn(dynAf, lastAf);
				}
			}
			lastAf.setArgUnderDecision(dynAf.getInitInstance().getArgUnderDecision());
			return lastAf;
		}

		private ArgumentationFramework removeAttackDyn(final DynamicArgumentationFramework af,
				ArgumentationFramework lastAf) {
			final Attack attack = attackRemovalTranslator.selectAttackToRemove(lastAf);
			lastAf = attackRemovalTranslator.translate(lastAf, attack);
			af.addTranslationAttackRemoval(attack, lastAf);
			return lastAf;
		}

		private ArgumentationFramework addAttackDyn(final DynamicArgumentationFramework af,
				ArgumentationFramework lastAf) {
			final Attack attack = newAttackTranslator.selectNewAttack(lastAf);
			lastAf = newAttackTranslator.translate(lastAf, attack);
			af.addTranlationNewAttack(attack, lastAf);
			return lastAf;
		}
		
	}


}
