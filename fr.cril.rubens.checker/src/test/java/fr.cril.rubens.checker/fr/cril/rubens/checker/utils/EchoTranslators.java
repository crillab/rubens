package fr.cril.rubens.checker.utils;

import fr.cril.rubens.specs.InstanceTranslator;

public final class EchoTranslators {
	
	private EchoTranslators() {
		// nothing to do here
	}
	
	public static InstanceTranslator<EchoInstance> newA() {
		return new InstanceTranslator<EchoInstance>() {

			@Override
			public boolean canBeAppliedTo(EchoInstance instance) {
				return true;
			}

			@Override
			public EchoInstance translate(EchoInstance instance) {
				return new EchoInstance(instance.toString()+"a");
			}
		};
	}
	
	public static InstanceTranslator<EchoInstance> newB() {
		return new InstanceTranslator<EchoInstance>() {

			@Override
			public boolean canBeAppliedTo(EchoInstance instance) {
				return true;
			}

			@Override
			public EchoInstance translate(EchoInstance instance) {
				return new EchoInstance(instance.toString()+"b");
			}
		};
	}

}
