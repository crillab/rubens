package fr.cril.rubens.checker.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

public class EchoInstance implements Instance {
	
	private final String str;

	public EchoInstance(final String str) {
		this.str = str;
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(".txt").collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(!extension.equals(".txt")) {
			return;
		}
		os.write(this.str.getBytes());
	}
	
	@Override
	public String toString() {
		return this.str;
	}

}
