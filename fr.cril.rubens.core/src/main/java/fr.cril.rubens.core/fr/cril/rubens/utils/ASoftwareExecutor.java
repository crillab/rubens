package fr.cril.rubens.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cril.rubens.specs.Instance;

/**
 * An abstract class used as a helper to launch external softwares on instances and get their result.
 * 
 * Subclasses must implement {@link ASoftwareExecutor#cliArgs(Path, Map, Instance)} to indicate which command line arguments must be set for a given execution.
 * 
 * @author Emmanuel Lonca - lonca@cril.fr
 *
 * @param <T> the type of the instances under consideration
 */
public abstract class ASoftwareExecutor<T extends Instance> {
	
	/** the logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(ASoftwareExecutor.class);
	
	/** the path to the software to launch */
	private final Path execPath;
	
	/** a thread pool used to clean temporary files */
	private static final ExecutorService TEMP_CLEANING_TH_POOL = Executors.newCachedThreadPool();
	
	/** the timeout; its unit is given by the timeoutUnit parameter */
	private long timeout = 1;
	
	/** the timeout unit */
	private TimeUnit timeoutUnit = TimeUnit.MINUTES;
	
	/**
	 * Adds a shutdown hook to wait for temporary files cleaning.
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			TEMP_CLEANING_TH_POOL.shutdown();
			try {
				TEMP_CLEANING_TH_POOL.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException(e);
			}
		}));
	}

	/**
	 * Builds a software executor for a given software.
	 * 
	 * @param execPath the path to the software to execute
	 */
	protected ASoftwareExecutor(final Path execPath) {
		this.execPath = execPath;
	}
	
	/**
	 * Executes the software on an instance and returns the result.
	 * 
	 * Software is executed using the command line arguments provided by {@link ASoftwareExecutor#cliArgs(Path, Map, Instance)}.
	 * 
	 * @param instance the instance
	 * @return the result (exit code, stdout, stderr)
	 */
	public SoftwareExecutorResult exec(final T instance) {
		Map<String, Path> instanceFiles;
		try {
			instanceFiles = writeInstanceToTemp(instance);
		} catch (IOException e) {
			LOGGER.error("an unexpected exception was thrown", e);
			return new SoftwareExecutorResult(127, false, "", "");
		}
		final List<String> cliArgs = cliArgs(this.execPath, instanceFiles, instance);
		return execSoftware(cliArgs, instanceFiles);
	}
	
	/**
	 * Returns the command line arguments to involve for an execution.
	 * 
	 * The second parameter is a mapping from instance files suffixes to the temporary file in which the content of the corresponding instance part has been copied.
	 * 
	 * The returned list of arguments must begin with the path to the software to execute. 
	 * 
	 * @param execLocation the path to the software to execute
	 * @param instanceFiles the instance files mapping
	 * @param instance the instance
	 * @return the list of arguments to pass to the executor
	 */
	protected abstract List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final T instance);

	private Map<String, Path> writeInstanceToTemp(final T instance) throws IOException {
		final Map<String, Path> instanceFiles = new HashMap<>();
		for(final String extension : instance.getFileExtensions()) {
			final Path p = Files.createTempFile("rubens-instance-", extension, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr--r--")));
			final OutputStream os = Files.newOutputStream(p);
			instance.write(extension, os);
			os.close();
			instanceFiles.put(extension, p);
		}
		return instanceFiles;
	}
	
	private SoftwareExecutorResult execSoftware(final List<String> cliArgs, final Map<String, Path> instanceFiles) {
		try {
			final StringBuilder stdoutBuilder = new StringBuilder();
			final StringBuilder stderrBuilder = new StringBuilder();
			final ProcessBuilder pBuilder = new ProcessBuilder(cliArgs);
			pBuilder.directory(this.execPath.getParent().toFile());
			final Process p = pBuilder.start();
			final ExecutorService thPool = Executors.newFixedThreadPool(2);
			launchStreamThread(thPool, p.getInputStream(), l -> stdoutBuilder.append(l).append('\n'));
			launchStreamThread(thPool, p.getErrorStream(), l -> stderrBuilder.append(l).append('\n'));
			p.getOutputStream().close();
			thPool.shutdown();
			thPool.awaitTermination(this.timeout, this.timeoutUnit);
			final boolean timeouted = !p.waitFor(this.timeout, this.timeoutUnit);
			p.destroy();
			final int status = p.waitFor();
			if(timeouted) {
				LOGGER.warn("subprocess exited by timeout");
			} else if(status != 0) {
				LOGGER.warn("subprocess exited with status {}", status);
			}
			return new SoftwareExecutorResult(status, timeouted, stdoutBuilder.toString(), stderrBuilder.toString());
		} catch(final IOException e) {
			throw new IllegalStateException(e);
		} catch(final InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException();
		} finally {
			synchronized(TEMP_CLEANING_TH_POOL) {
				TEMP_CLEANING_TH_POOL.submit(() -> {
					for(final Path p : instanceFiles.values()) {
						try {
							Files.deleteIfExists(p);
						} catch(IOException e) {
							LOGGER.error("an unexpected I/O exception occurred", e);
						}
					}
				});
			}
		}
	}
	
	/**
	 * Launches a stream used to handle {@link ProcessBuilder} input streams.
	 * 
	 * The stream is read line per line; each line is then process by the provided {@link Consumer}.
	 * 
	 * @param the thread pool used to launch this thread
	 * @param is the input stream
	 * @param lineHandler the line consumer
	 */
	private static void launchStreamThread(final ExecutorService threadPool, final InputStream is, final Consumer<String> lineHandler) {
		threadPool.submit(() -> {
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
		});
	}
	
	public void setTimeout(final long timeout, final TimeUnit timeoutUnit) {
		this.timeout = timeout;
		this.timeoutUnit = timeoutUnit;
	}
	
}
