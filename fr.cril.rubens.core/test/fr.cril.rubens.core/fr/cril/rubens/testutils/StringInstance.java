package fr.cril.rubens.testutils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

public class StringInstance implements Instance {
	
	private final String str;
	
	public StringInstance(final String str) {
		this.str = str;
	}
	
	public String str() {
		return this.str;
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(".str").collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(".str".equals(extension)) {
			os.write(this.str.getBytes());
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public String toString() {
		return this.str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
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
		StringInstance other = (StringInstance) obj;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}

}
