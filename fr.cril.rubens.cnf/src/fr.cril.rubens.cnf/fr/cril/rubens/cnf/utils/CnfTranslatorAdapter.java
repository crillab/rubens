package fr.cril.rubens.cnf.utils;

import java.lang.reflect.InvocationTargetException;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.specs.InstanceTranslator;
import fr.cril.rubens.specs.TestGeneratorFactory;

/**
 * An adapter for CNF translators.
 * 
 * As several {@link TestGeneratorFactory} consider instances whose problem is encoded by a CNF formula (but the solutions depend on the problem),
 * the translators can be reused for many kinds of instances.
 * This adapter simply decorate the adapter for types to match to the correct output instance type.
 * 
 * The output instance type may provide a copy constructor taking a {@link CnfInstance} object as parameter.
 * If it is not the case, an {@link IllegalArgumentException} will be raised when calling {@link CnfTranslatorAdapter#translate(CnfInstance)}.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <I> the CNF instance input type
 * @param <O> the CNF instance output type
 */
public class CnfTranslatorAdapter<I extends CnfInstance, O extends I> implements InstanceTranslator<O> {
	
	private final InstanceTranslator<I> adaptedTranslator;
	
	public CnfTranslatorAdapter(final InstanceTranslator<I> adaptedTranslator) {
		this.adaptedTranslator = adaptedTranslator;
	}

	@Override
	public boolean canBeAppliedTo(final O instance) {
		return this.adaptedTranslator.canBeAppliedTo(instance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public O translate(O instance) {
		try {
			return (O) instance.getClass().getConstructor(CnfInstance.class).newInstance(this.adaptedTranslator.translate(instance));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Illegal reflection using "+this.getClass().getCanonicalName()
					+ ": you must instantiate a constructor for "+instance.getClass().getCanonicalName()+" taking a "
					+ CnfInstance.class.getCanonicalName()+" instance as parameter.");
		}
	}

}
