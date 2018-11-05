package fr.cril.rubens.arg.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

/**
 * A class used to handle argumentation frameworks.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ArgumentationFramework implements Instance {
	
	/** file extension associated with the Aspartix format */
	private static final String APX_EXT = ".apx";
	
	/** file extension associated with the set of extensions */
	private static final String EXTS_EXT = ".exts";
	
	/** the set of arguments involved in the framework */
	private final ArgumentSet arguments;
	
	/** the set of attacks of the framework */
	private final AttackSet attacks;
	
	/** the set of extensions associated with this framework */
	private final ExtensionSet extensions;
	
	/**
	 * Builds a new empty instance of {@link ArgumentationFramework}.
	 */
	public ArgumentationFramework() {
		this.arguments = ArgumentSet.getInstance(Collections.emptySet());
		this.attacks = AttackSet.getInstance(Collections.emptySet());
		this.extensions = ExtensionSet.getInstance(Collections.emptySet());
	}
	
	/**
	 * Builds a new argumentation frameworks given all its characteristics.
	 * 
	 * @param arguments the set of arguments
	 * @param attacks the set of attacks
	 * @param extensions the set of extensions
	 */
	public ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions) {
		if(arguments == null || attacks == null || extensions == null) {
			throw new IllegalArgumentException();
		}
		this.arguments = arguments;
		this.attacks = attacks;
		this.extensions = extensions;
	}

	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(APX_EXT, EXTS_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(os == null) {
			throw new IllegalArgumentException();
		}
		if(APX_EXT.equals(extension)) {
			writeInstance(os);
		} else if(EXTS_EXT.equals(extension)) {
			writeExtensions(os);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private void writeInstance(final OutputStream os) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			final StringBuilder builder = new StringBuilder();
			this.arguments.stream().map(Argument::getName).forEach(s -> builder.append("arg(").append(s).append(").\n"));
			this.attacks.stream().map(att -> "att("+att.getAttacker().getName()+","+att.getAttacked().getName()+").\n").forEach(builder::append);
			writer.write(builder.toString());
		}
	}

	private void writeExtensions(final OutputStream os) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			final Optional<String> content = this.extensions.stream().map(this::extensionToString).reduce((acc, ext) -> acc+",\n"+ext);
			if(!content.isPresent()) {
				writer.write("[\n]\n");
			} else {
				writer.write("[\n");
				writer.write(content.get());
				writer.write("\n]\n");
			}
		}
	}
	
	private String extensionToString(final ArgumentSet extension) {
		return "["+extension.stream().map(Argument::getName).reduce((acc, ext) -> acc+","+ext).orElse("")+"]";
	}
	
	/**
	 * Returns the set of arguments contained in this instance.
	 * 
	 * @return the set of arguments contained in this instance
	 */
	public ArgumentSet getArguments() {
		return this.arguments;
	}
	
	/**
	 * Returns the set of attacks contained in this instance.
	 * 
	 * @return the set of attacks contained in this instance
	 */
	public AttackSet getAttacks() {
		return this.attacks;
	}
	
	/**
	 * Returns the set of extensions this framework admits.
	 * 
	 * @return the set of extensions this framework admits
	 */
	public ExtensionSet getExtensions() {
		return this.extensions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arguments.hashCode();
		result = prime * result + attacks.hashCode();
		result = prime * result + extensions.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArgumentationFramework)) {
			return false;
		}
		final ArgumentationFramework other = (ArgumentationFramework) obj;
		return this.arguments.equals(other.arguments) && this.attacks.equals(other.attacks) && this.extensions.equals(other.extensions);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append(this.arguments);
		final String comma = ", ";
		builder.append(comma);
		builder.append(this.attacks);
		builder.append(comma);
		builder.append(this.extensions);
		builder.append(']');
		return builder.toString();
	}

}
