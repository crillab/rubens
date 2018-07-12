package fr.cril.rubens.cnf.mc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.cril.rubens.cnf.core.CnfInstance;
import fr.cril.rubens.specs.Instance;

/**
 * An {@link Instance} implementation dedicated to the model counting problem for CNF formulas.
 * 
 * This class is built on top of the {@link CnfInstance}, and mainly translates the set of models into its size.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class ModelCountingCnfInstance extends CnfInstance {
	
	/** the default extension for files storing the model count */
	public static final String MC_EXT = ".mc";
	
	/**
	 * Builds a new model counting instance.
	 */
	public ModelCountingCnfInstance() {
		super();
	}
	
	/**
	 * The copy constructor of a {@link CnfInstance}.
	 * 
	 * @param instance the instance to copy
	 */
	public ModelCountingCnfInstance(final CnfInstance instance) {
		super(instance);
	}
	
	@Override
	public Collection<String> getFileExtensions() {
		return Stream.of(CNF_EXT, MC_EXT).collect(Collectors.toList());
	}

	@Override
	public void write(final String extension, final OutputStream os) throws IOException {
		if(CNF_EXT.equals(extension)) {
			super.writeCNF(os);
		} else if(MC_EXT.equals(extension)) {
			os.write(Integer.toString(super.models().size()).getBytes());
			os.write('\n');
		} else {
			throw new IllegalArgumentException("unknown extension "+extension);
		}
	}
	
}
