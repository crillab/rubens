package fr.cril.rubens.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.core.TestGenerator;
import fr.cril.rubens.specs.Instance;

/**
 * Application entry-point for RUBENS instance generator.
 *   
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class Generator {
	
	public static final int STATUS_IO_EXCEPTION_DURING_GENERATION = 3;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

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
		final long startTime = System.currentTimeMillis();
		if(this.statusCode != 0) {
			if(statusCode == STATUS_IO_EXCEPTION_DURING_GENERATION) {
				LOGGER.error("an error occurred during the last generation process using this generator instance; ignoring this one");
			} else {
				LOGGER.error("an error occurred while parsing this generator parameters; ignoring the generation process");
			}
			return;
		}
		final TestGenerator<Instance> generator = new TestGenerator<>(this.generatorOptions.getFactory());
		generator.computeToDepth(this.generatorOptions.getMaxDepth(), this::outputInstance);
		final Supplier<String> strTimeSupplier = () -> String.format("%.3f", (System.currentTimeMillis() - startTime)/1000f);
		LOGGER.info("generated {} instances in {}s", this.instanceCount, strTimeSupplier.get());
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
