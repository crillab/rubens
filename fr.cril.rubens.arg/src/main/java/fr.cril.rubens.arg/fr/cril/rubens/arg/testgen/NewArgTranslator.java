package fr.cril.rubens.arg.testgen;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.arg.core.Argument;
import fr.cril.rubens.arg.core.ArgumentSet;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.ArgumentationFrameworkTranslation;
import fr.cril.rubens.arg.core.ExtensionSet;
import fr.cril.rubens.specs.InstanceTranslator;

/**
 * An instance translator that adds new arguments to an existing frameworks.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class NewArgTranslator implements InstanceTranslator<ArgumentationFramework> {
	
	/** the integer index used to build argument names */
	private int nextArgIndex = 0;
	
	/** the algorithm used to compute extensions */
	private EExtensionSetComputer extensionSetComputer;
	
	/**
	 * Builds a new instance of this translator given the algorithm used to compute extensions.
	 * 
	 * @param extensionSetComputer the algorithm used to compute extensions
	 */
	public NewArgTranslator(final EExtensionSetComputer extensionSetComputer) {
		this.extensionSetComputer = extensionSetComputer;
	}

	@Override
	public boolean canBeAppliedTo(final ArgumentationFramework instance) {
		return true;
	}

	@Override
	public ArgumentationFramework translate(final ArgumentationFramework instance) {
		Argument newArg0 = null;
		while(newArg0 == null) {
			final Argument a = Argument.getInstance("a"+(this.nextArgIndex++));
			if(!instance.getArguments().contains(a)) {
				newArg0 = a;
			}
		}
		final Argument newArg = newArg0;
		final ArgumentSet newArguments = Stream.concat(instance.getArguments().stream(), Stream.of(newArg)).collect(ArgumentSet.collector());
		final ExtensionSet newExtensions = this.extensionSetComputer.compute(newArguments, instance.getAttacks());
		final ArgumentationFramework af = new ArgumentationFramework(newArguments, instance.getAttacks(), newExtensions, instance, ArgumentationFrameworkTranslation.newArgument(newArg));
		final List<Argument> argList = newArguments.stream().collect(Collectors.toList());
		Collections.shuffle(argList);
		af.setArgUnderDecision(argList.get(0));
		return af;
	}

}
