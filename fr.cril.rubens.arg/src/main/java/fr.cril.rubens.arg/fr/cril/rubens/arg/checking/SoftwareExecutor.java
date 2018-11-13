package fr.cril.rubens.arg.checking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.arg.core.AArgumentationFrameworkGraph;
import fr.cril.rubens.arg.core.ArgumentationFramework;

/**
 * A class used to execute an external software for argumentation framework problems.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 */
public class SoftwareExecutor {
	
	/** the logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareExecutor.class);
	
	private SoftwareExecutor() {
		// nothing to do here
	}
	
	/**
	 * Executes the software on an instance and returned its output.
	 * 
	 * The problem name is the one to pass to the "-p" parameter of the argumentation solver.
	 * 
	 * @param exec the software location
	 * @param problem the problem name
	 * @param instance the instance
	 * @return the software output
	 */
	public static String execSoftware(final String exec, final String problem, final ArgumentationFramework instance) {
		Path apxPath;
		try {
			apxPath = writeInstanceToTemp(instance);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final List<String> cliArgs = cliArgs(exec, problem, instance, apxPath);
		final String result = doExecSoftware(exec, apxPath, cliArgs, false);
		if(result.equals("")) {
			try {
				apxPath = writeInstanceToTemp(instance);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			return doExecSoftware(exec, apxPath, cliArgs, true);
		}
		return result;
	}
	
	/**
	 * Executes the software on an instance and returned its output.
	 * 
	 * It sometimes happen the standard output stream contains no data although it should.
	 * This method provides a flag allowing to wait one second before waiting for the subprocess end;
	 * this sleeping time seems to fix the problem.
	 * 
	 * @param exec the software location
	 * @param instance the instance
	 * @param wait the waiting flag
	 * @return the solver output
	 */
	private static String doExecSoftware(final String exec, final Path apxPath, final List<String> cliArgs, final boolean wait) {
		try {
			final ProcessBuilder pBuilder = new ProcessBuilder(cliArgs);
			pBuilder.directory(Paths.get(exec).getParent().toFile());
			final Process p = pBuilder.start();
			final StringBuilder builder = new StringBuilder();
			launchStreamThread(p.getInputStream(), l -> builder.append(l).append('\n'));
			launchStreamThread(p.getErrorStream(), l -> LOGGER.warn("software wrote to stderr: {}", l));
			p.getOutputStream().close();
			if(wait) {
				Thread.sleep(500);
			}
			p.waitFor();
			Files.delete(apxPath);
			return builder.toString();
		} catch(final IOException e) {
			throw new IllegalStateException(e);
		} catch(final InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException();
		} finally {
			try {
				if(Files.exists(apxPath)) {
					Files.delete(apxPath);
				}
			} catch(IOException e) {
				// nothing to do here
			}
		}
	}

	private static List<String> cliArgs(final String exec, final String problem, final ArgumentationFramework instance, Path apxPath) {
		final List<String> cliArgs = Stream.of(exec, "-fo", "apx", "-f", apxPath.toAbsolutePath().toString(), "-p", problem).collect(Collectors.toList());
		if(Stream.of("DC-", "DS-").anyMatch(problem::startsWith)) {
			Stream.of("-a", instance.getArgUnderDecision().getName()).forEach(cliArgs::add);
		}
		return cliArgs;
	}

	private static Path writeInstanceToTemp(final ArgumentationFramework instance) throws IOException {
		Path apxPath;
		apxPath = Files.createTempFile("rubens-arg-", ".tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")));
		instance.write(AArgumentationFrameworkGraph.APX_EXT, Files.newOutputStream(apxPath));
		return apxPath;
	}
	
	/**
	 * Launches a stream used to handle {@link ProcessBuilder} input streams.
	 * 
	 * The stream is read line per line; each line is then process by the provided {@link Consumer}.
	 * 
	 * @param is the input stream
	 * @param lineHandler the line consumer
	 */
	private static void launchStreamThread(final InputStream is, final Consumer<String> lineHandler) {
		new Thread(() -> {
			try(final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
				String line;
				while((line = reader.readLine()) != null) {
					lineHandler.accept(line);
				}
				is.close();
			} catch (IOException e) {
				LOGGER.warn("got an I/O exception while reading software output with reason: {}", e.getMessage());
				return;
			}
		}).start();
	}

}
