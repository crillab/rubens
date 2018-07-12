package fr.cril.rubens.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import fr.cril.rubens.core.TestGenerator;
import fr.cril.rubens.specs.Instance;

/**
 * Application entry-point for RUBENS instance generator.
 *   
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class Generator {
	
	public static final int STATUS_IO_EXCEPTION_DURING_GENERATION = 3;

	private int instanceCount = 0;
	
	private int statusCode = 0;

	private final GeneratorOptionsReader generatorOptions;

	/**
	 * Launches the generator. See documentation for command line arguments.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		final Generator generator = new Generator(args);
		generator.generate();
		System.exit(generator.statusCode);
	}
	
	/**
	 * Builds a new generator based on the parameters provided through the command line interface.
	 * See documentation for command line arguments.
	 * 
	 * @param args the parameters of the command line interface
	 */
	public Generator(final String[] args) {
		this.generatorOptions = GeneratorOptionsReader.getInstance();
		generatorOptions.loadOptions(args);
		if(generatorOptions.mustExit()) {
			this.statusCode = generatorOptions.exitStatus();
		}
	}

	/**
	 * Builds a new generator based on the parameters provided through a {@link GeneratorOptionsReader} object.
	 * 
	 * @param generatorOptions the parameters of the generator to build
	 */
	public Generator(final GeneratorOptionsReader generatorOptions) {
		this.generatorOptions = generatorOptions;
	}

	/**
	 * Launches the generation process.
	 */
	public void generate() {
		if(this.statusCode != 0) {
			return;
		}
		final TestGenerator<Instance> generator = new TestGenerator<>(this.generatorOptions.getFactory());
		generator.computeToDepth(this.generatorOptions.getMaxDepth(), this::outputInstance);
	}

	private void outputInstance(final Instance instance) {
		if(this.statusCode != 0) {
			return;
		}
		try {
			final Collection<String> extensions = instance.getFileExtensions();
			for(final String ext : extensions) {
				instance.write(ext, new FileOutputStream(new File(this.generatorOptions.getOutputDirectory(), instanceCount+ext)));
			}
			this.instanceCount++;
		} catch (IOException e) {
			this.statusCode = STATUS_IO_EXCEPTION_DURING_GENERATION;
		}
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * Returns the number of instances built by the generator.
	 * 
	 * @return the number of instances built by the generator
	 */
	public int getInstanceCount() {
		return this.instanceCount;
	}

}
