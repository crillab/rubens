package fr.cril.rubens.testutils;

import fr.cril.rubens.specs.InstanceTranslator;

public class StringConcatTranslator implements InstanceTranslator<StringInstance> {
	
	private final String concat;

	public StringConcatTranslator(final String concat) {
		this.concat = concat;
	}

	@Override
	public boolean canBeAppliedTo(final StringInstance instance) {
		return true;
	}

	@Override
	public StringInstance translate(final StringInstance instance) {
		return new StringInstance(instance.str()+this.concat);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concat == null) ? 0 : concat.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringConcatTranslator other = (StringConcatTranslator) obj;
		if (concat == null) {
			if (other.concat != null)
				return false;
		} else if (!concat.equals(other.concat))
			return false;
		return true;
	}
	
}