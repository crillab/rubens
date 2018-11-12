package fr.cril.rubens.arg.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.specs.Instance;

/**
 * A class used to handle argumentation frameworks.
 * 
 * In case a framework is the result of applying a sequence of translations from the empty framework,
 * it is advised to instantiate it using the {@link ArgumentationFramework#ArgumentationFramework(ArgumentSet, AttackSet, ExtensionSet, ArgumentationFramework, ArgumentationFrameworkTranslation)}
 * constructor to build the new one; this will keep the translation history.
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
	
	/** the argument under consideration while considering DC/DS queries */
	private Argument argUnderDecision;
	
	/** the history of translation applied from the initial framework to this one */
	private final List<ArgumentationFrameworkTranslation> translationHistory;
	
	/**
	 * Builds a new empty instance of {@link ArgumentationFramework}.
	 */
	public ArgumentationFramework() {
		this(ArgumentSet.getInstance(Collections.emptySet()), AttackSet.getInstance(Collections.emptySet()), ExtensionSet.getInstance(Collections.emptySet()),
				Stream.of(ArgumentationFrameworkTranslation.emptyAF()).collect(Collectors.toUnmodifiableList()));
	}
	
	/**
	 * Builds a new argumentation frameworks given all its characteristics.
	 * 
	 * @param arguments the set of arguments
	 * @param attacks the set of attacks
	 * @param extensions the set of extensions
	 */
	public ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions) {
		this(arguments, attacks, extensions, null);
	}
	
	/**
	 * Builds a new argumentation frameworks given all its characteristics and the translation that created it.
	 * 
	 * In case a framework is the result of applying a sequence of translations from the empty framework,
	 * it is advised to instantiate it using this constructor to build the new one; this will keep the translation history.
	 * 
	 * @param arguments the set of arguments
	 * @param attacks the set of attacks
	 * @param extensions the set of extensions
	 * @param oldFramework the previous argumentation framework
	 * @param translation the translation used to translate the old framework to this new one
	 */
	public ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions, final ArgumentationFramework oldFramework,
			final ArgumentationFrameworkTranslation translation) {
		this(arguments, attacks, extensions, Stream.concat(oldFramework.translationHistory.stream(), Stream.of(translation)).collect(Collectors.toUnmodifiableList()));
	}
	
	private ArgumentationFramework(final ArgumentSet arguments, final AttackSet attacks, final ExtensionSet extensions, List<ArgumentationFrameworkTranslation> history) {
		if(arguments == null || attacks == null || extensions == null) {
			throw new IllegalArgumentException();
		}
		this.arguments = arguments;
		this.attacks = attacks;
		this.extensions = extensions;
		this.translationHistory = history;
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
	
	/**
	 * Sets the argument under consideration while considering DC/DS queries.
	 *
	 * @param arg the argument
	 */
	public void setArgUnderDecision(final Argument arg) {
		if(arg == null || !this.arguments.contains(arg)) {
			throw new IllegalArgumentException();
		}
		this.argUnderDecision = arg;
	}
	
	/**
	 * Returns the argument under consideration while considering DC/DS queries.
	 *
	 * @return the argument under consideration while considering DC/DS queries
	 */
	public Argument getArgUnderDecision() {
		return this.argUnderDecision;
	}
	
	/**
	 * Returns the translation history from the root instance that have lead to this instance.
	 * 
	 * If this instance was obtained by {@link ArgumentationFramework#ArgumentationFramework(ArgumentSet, AttackSet, ExtensionSet)},
	 * this method will return <code>null</code>.
	 * 
	 * @return the translation history from the root instance that have lead to this instance
	 */
	public List<ArgumentationFrameworkTranslation> getTranslationHistory() {
		return this.translationHistory;
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
