package fr.cril.rubens.arg.checking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.arg.core.AArgumentationFrameworkGraph;
import fr.cril.rubens.arg.core.ArgumentationFramework;
import fr.cril.rubens.arg.core.DynamicArgumentationFramework;

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
		String[] result = doExecSoftware(exec, apxPath, null, cliArgs, false);
		if(result[0].isEmpty() || !result[1].isEmpty()) {
			try {
				apxPath = writeInstanceToTemp(instance);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			result = doExecSoftware(exec, apxPath, null, cliArgs, true);
		}
		if(!result[1].isEmpty()) {
			Arrays.stream(result[1].split("\n")).forEach(l -> LOGGER.warn("software wrote to stderr: {}", l));
		}
		return result[0];
	}
	
	/**
	 * Executes the software on an instance and a dynamics file and return its output.
	 * 
	 * The problem name is the one to pass to the "-p" parameter of the argumentation solver.
	 * 
	 * @param exec the software location
	 * @param problem the problem name
	 * @param instance the instance
	 * @return the software output
	 */
	public static String execSoftwareForDynamicProblem(final String exec, final String problem, final DynamicArgumentationFramework instance) {
		Path apxPath;
		Path apxmPath;
		try {
			apxPath = writeInstanceToTemp(instance.getInitInstance());
			apxmPath = writeDynamicsToTemp(instance);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final List<String> cliArgs = cliArgs(exec, problem, instance.getInitInstance(), apxPath, apxmPath);
		String[] result = doExecSoftware(exec, apxPath, apxmPath, cliArgs, false);
		if(result[0].isEmpty() || !result[1].isEmpty()) {
			try {
				apxPath = writeInstanceToTemp(instance.getInitInstance());
				apxmPath = writeDynamicsToTemp(instance);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			result = doExecSoftware(exec, apxPath, apxmPath, cliArgs, true);
		}
		if(!result[1].isEmpty()) {
			Arrays.stream(result[1].split("\n")).forEach(l -> LOGGER.warn("software wrote to stderr: {}", l));
		}
		return result[0];
	}
	
	private static String[] doExecSoftware(final String exec, final Path apxPath, final Path apxmPath, final List<String> cliArgs, final boolean wait) {
		try {
			final StringBuilder stdoutBuilder = new StringBuilder();
			final StringBuilder stderrBuilder = new StringBuilder();
			final ProcessBuilder pBuilder = new ProcessBuilder(cliArgs);
			pBuilder.directory(Paths.get(exec).getParent().toFile());
			final Process p = pBuilder.start();
			launchStreamThread(p.getInputStream(), l -> stdoutBuilder.append(l).append('\n'));
			launchStreamThread(p.getErrorStream(), l -> stderrBuilder.append(l).append('\n'));
			p.getOutputStream().close();
			if(wait) {
				Thread.sleep(500);
			}
			final int status = p.waitFor();
			if(status != 0) {
				LOGGER.warn("subprocess exited with status {}", status);
			}
			return new String[] {stdoutBuilder.toString(), stderrBuilder.toString()};
		} catch(final IOException e) {
			throw new IllegalStateException(e);
		} catch(final InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException();
		} finally {
			new Thread(() -> {
				try {
					if(Files.exists(apxPath)) {
						Thread.sleep(500);
						Files.delete(apxPath);
					}
					if(apxmPath != null && Files.exists(apxmPath)) {
						Thread.sleep(500);
						Files.delete(apxmPath);
					}
				} catch(IOException e) {
					LOGGER.error("an unexpected I/O exception occurred", e);
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					LOGGER.error("an unexpected thread interruption occurred", e);
				}
			}).start();
		}
	}
	
	private static List<String> cliArgs(final String exec, final String problem, final ArgumentationFramework instance, final Path apxPath) {
		return cliArgs(exec, problem, instance, apxPath, null);
	}
	
	private static List<String> cliArgs(final String exec, final String problem, final ArgumentationFramework instance, final Path apxPath, final Path apxmPath) {
		final List<String> cliArgs = Stream.of(exec, "-fo", "apx", "-f", apxPath.toAbsolutePath().toString(), "-p", problem).collect(Collectors.toList());
		if(apxmPath != null) {
			Stream.of("-m", apxmPath.toAbsolutePath().toString()).forEach(cliArgs::add);
		}
		if(Stream.of("DC-", "DS-").anyMatch(problem::startsWith)) {
			Stream.of("-a", instance.getArgUnderDecision().getName()).forEach(cliArgs::add);
		}
		return cliArgs;
	}

	private static Path writeInstanceToTemp(final ArgumentationFramework instance) throws IOException {
		Path apxPath;
		apxPath = Files.createTempFile("rubens-arg-", ".tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr--r--")));
		final OutputStream os = Files.newOutputStream(apxPath);
		instance.write(AArgumentationFrameworkGraph.APX_EXT, os);
		os.close();
		return apxPath;
	}
	
	private static Path writeDynamicsToTemp(final DynamicArgumentationFramework instance) throws IOException {
		Path apxmPath;
		apxmPath = Files.createTempFile("rubens-arg-dyn-", ".tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr--r--")));
		final OutputStream os = Files.newOutputStream(apxmPath);
		instance.write(DynamicArgumentationFramework.APXM_EXT, os);
		os.close();
		return apxmPath;
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
