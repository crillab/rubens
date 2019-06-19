# RUBENS: RUle-Based Experiment generation for Never-failing Softwares

[![Build Status](https://travis-ci.org/crillab/rubens.svg?branch=master)](https://travis-ci.org/crillab/rubens)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=fr.cril.rubens%3Afr.cril.rubens.pom&metric=alert_status)](https://sonarcloud.io/dashboard?id=fr.cril.rubens%3Afr.cril.rubens.pom)
[![License](https://img.shields.io/github/license/crillab/rubens.svg)](https://github.com/crillab/rubens/blob/master/LICENSE)



## What is RUBENS ?

RUBENS is a library designed to generate test cases in an automatic way using translation rules. It is delivered with some builtin test generators and an interface conceived in order to create new ones with few effort.

A test generator is built by two components:

* an initial (trivial) test instance, containing both a test case and its expected set of solutions;
* a set of instance translators, each of them taking an instance and its set of solutions as input, and returning another test case with its own set of solutions.

Given a test generator, one can use the first tool provided by RUBENS, which is used to generate several files describing instances and their set of solutions. These files can then be used by external tools to check the reliability of pieces of software.

In case the code under test is a complete software, one can use RUBENS to directly test this software. In addition to the test generator, it is then sufficient to implement two methods:

* one to execute the software under test on a single instance (in most cases, giving the command line arguments is sufficient for this step);
* one to check the output of the software under test against the expected set of solutions.

This software (the RUBENS checker) then generates test cases, and executes the software under test on them. As soon as an unexpected result is got, an error message is thrown and the instance (including the expected result) is written on the disk.


## Software requirements

RUBENS requires `java` (with version at least 11) and `maven` to be installed. The additional dependencies will then be automatically installed by `maven`.

Note that a bug in the javadoc tool provided with jdk-11 prevents the documentation to be generated (see [this ticket](https://bugs.openjdk.java.net/browse/JDK-8208269) for more information). You need jdk-12 to generate the javadoc.


## Usage

Common usages:

* `java -jar rubens-{generator, checker}.jar -h`: display help and exit
* `java -jar rubens-{generator, checker}.jar -l`: list available generation/checking methods and exit

Generator usage:

* `java -jar rubens-generator.jar -m <method> -o <dir> [-d <depth>]` applies the generation method `<method>`, output directory is `<dir>`, the tree generation depth is `<depth>` (a positive integer).

Checker usage:

* `java -jar rubens-checker.jar -m <method> -o <dir> -e <softwareExec> [-d <depth>] [-c <methodOpts>]`: applies the checking method `<method>` on software which executable is located at `<softwareExec>`, output directory is `<dir>`, the tree generation depth is `<depth>` (a positive integer), special methods arguments are `<methodOpts>`.

Special methods arguments are `key=value` assignments split by commas (`,`) changing the behavior of the selected checker. Each checker has its own method arguments. The default tree generation depth is fixed to 10.


## Builtin test generators

You can access the list of builtin test generators with the command `java -jar rubens-generator.jar -l`.

At this time, there are two families of builtin generators:

* CNF family:
  * CNF: generates DIMACS formatted CNF formulas and their set of models (SAT instances),
  * MCCNF: generates DIMACS formatted CNF formulas and number of models (model counting instances),
  * WMCNF: generates DIMACS formatted CNF formulas, literal weights, and their set of weighted models (weighted SAT instances),
  * WMCCNF: generates DIMACS formatted CNF formulas, literal weights, and the weighted number of models (weighted model counting instances);
* Argument framework family:
  * ARG-CO: argumentation frameworks and their set of complete extensions,
  * ARG-GR: argumentation frameworks and their set of grounded extensions,
  * ARG-PR: argumentation frameworks and their set of preferred extensions,
  * ARG-ST: argumentation frameworks and their set of stable extensions,
  * ARG-SST: argumentation frameworks and their set of semistable extensions,
  * ARG-STG: argumentation frameworks and their set of stage extensions,
  * ARG-ID: argumentation frameworks and their set of ideal extensions,
  * D3: argumentation frameworks and their Dung's Triathlon result (see ICCMA 2017 competition).


## Builtin checkers

You can access the list of builtin test generators with the command `java -jar rubens-checker.jar -l`.

At this time, there are two families of builtin checkers:

* CNF family:
  * SAT: checks a SAT solver fitting SAT competitions rules,
  * sharpSAT: checks a CNF model counter using same I/O conventions than SAT solvers except that the `s` line gives the number of models and there is no `v` line,
  * DDNNF: checks a d-DNNF compiler taking as input a DIMACS formatted CNF formula and outputting a d-DNNF following the format of the `c2d` compiler;
* Argumentation Framework family:
  * XX-YY, where XX in {SE, EE, DC, DS} and YY in {CO, GR, PR, ST, SST, STG, ID}: computes a task for a semantics on an AF following ICCMA competition rules,
  * D3: computes the D3 task (see ICCMA 2017),
  * XX-YY-D, where XX in {SE, EE, DC, DS} and YY in {CO, GR, PR, ST}: computes a task with dynamics for a semantics on an AF following ICCMA competition rules,
  * ICCMA2019, ICCMA2019: performs all computations involved in ICCMA 2017 and 2019 competitions.

Argumentation framework checker are able to handle both ICCMA 2017 and 2019 (default) output formats. The format can be changed by adding `-c outputFormat=ICCMA17` to the command line.


## Implementing a custom instance generator

In this section, we describe how to build a simple test generator used to check a software which counts and displays the number of words contained in a text file (just like the `wc -w` UNIX command). For the sake of simplicity, we consider text files containing only lower case letters and spaces, such that it is not allowed to have more than one consecutive space characters.

### building a dedicated java module

We create a java module (`fr.cril.rubens.wcw`) dedicated to this checker inside RUBENS directory, and configure both `module-info.java` and `pom.xml` files to include the `fr.cril.rubens.core` module. We then create the package `fr.cril.rubens.wcw` and exports it in `module-info.java`. After this step, the structure of the project should match this one:

```
fr.cril.rubens/
├── fr.cril.rubens.arg/
│   [...]
├── fr.cril.rubens.checker/
│   [...]
├── fr.cril.rubens.cnf/
│   [...]
├── fr.cril.rubens.core/
│   [...]
├── fr.cril.rubens.generator/
│   [...]
└── fr.cril.rubens.wcw/
    ├── pom.xml
    └── src/
        └── main/
            └── java
                ├── fr.cril.rubens.wcw
                │   └── fr
                │       └── cril
                │           └── rubens
                │               └── wcw
                └── module-info.java
```

The `fr.cril.rubens.wcw/pom.xml` file can be a copy of `fr.cril.rubens.arg/pom.xml` where the `artifactId`, `name` and `description` fields have been modified:

```xml
<artifactId>fr.cril.rubens.wcw</artifactId>
<name>RUBENS module the wc -w command</name>
<description>A simple module intended to check the output of softwares counting the number of words a file contains.</description>
```

The content of `fr.cril.rubens.wcw/src/main/java/module-info.java` should match this one:

```java
module fr.cril.rubens.wcw {
	
	requires fr.cril.rubens.core;
	
	exports fr.cril.rubens.wcw;

}
```

### implementing the instance type

First, we have to write a class (`fr.cril.rubens.wcw.WCWInstance`) defining a new instance type. Our instances are made of a `String` (the content of a file we want to count the number of words) and override the `fr.cril.rubens.specs.Instance` interface.

For each instance, RUBENS will generate some files describing the test case and the set of expected answers. The two methods of the `Instance` interface are used to indicate to RUBENS how to encode an instance and its expected solutions using as many files as you need. 

The first method is `getFileExtensions`, in which you must return one suffix per file you need; for our module, we will store the text content in `.txt` files, and the number of words in `.n` files. Returning a list of these two file extensions, RUBENS will create for each generated instance the files `XXX.txt` and `XXX.n`.

The second method is `write` which takes as input one of the file suffixes returned by `getFileExtensions` and an output stream. In this method, you must write into the output stream the content corresponding to the file extension. In our module, we must write the text content (resp. the number of words) into the output stream if the file extension is `.txt` (resp. `.n`). You can find below the full content of our `Instance` implementation.

```java
public class WCWInstance implements Instance {
	
	private final String content;

	public WCWInstance(final String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public long wordCount() {
		return Arrays.stream(this.content.split(" ")).filter(s -> !s.isEmpty()).count();
	}
	
	public Collection<String> getFileExtensions() {
		return Stream.of(".txt", ".n").collect(Collectors.toList());
	}
	
	public void write(final String extension, final OutputStream os) throws IOException {
		switch (extension) {
		case ".txt":
			write(os, this.content);
			break;
		case ".n":
			write(os, Long.toString(wordCount()));
			break;
		default:
			throw new IllegalArgumentException("unknown extension: "+extension);
		}
	}
	
	private void write(final OutputStream os, final String content) throws IOException {
		try(final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
			writer.write(content);
		}
	}

}
```

### implementing the instance generator

Now, we writes the test generator class (`fr.cril.rubens.wcw.WCWTestGenerator` which implements `fr.cril.rubens.specs.TestGeneratorFactory` for the `WCWInstance` type. There are three pieces of code to write here.

First, the `initInstance` method, in which we have to return the root instance of the generation process. In our case, we have to return a `WCWInstance` for an empty string.

Then, we have to describe to RUBENS the available translations, by implementing the `translators` method. This method returns a list of `InstanceTranslator` for the instance type under consideration. Each translator must define two methods:

* `canBeAppliedTo` which returns a Boolean indicating if the translator can be applied to the instance given by its parameter
* `translate` which return the instance obtained by applying the underlying translation to the instance given by its parameter.

We have two translators in our class: one that adds a lowercase letter (which can be applied on any instance), and one that adds a space characters (which cannot be applied on space-terminated content).

Finally, the class must be annotated by a `ReflectorParam` which defines the name of the generator (see the section about reflection below). Here is the code of our `TestGenerator`.

```java
@ReflectorParam(name="WCW")
public class WCWTestGenerator implements TestGeneratorFactory<WCWInstance> {
	
	private static final Random RND = new Random(System.currentTimeMillis());

	@Override
	public WCWInstance initInstance() {
		return new WCWInstance("");
	}

	@Override
	public List<InstanceTranslator<WCWInstance>> translators() {
		return Stream.of(new NewLetterTranslator(), new NewSpaceTranslator()).collect(Collectors.toList());
	}
	
	private class NewLetterTranslator implements InstanceTranslator<WCWInstance> {

		@Override
		public boolean canBeAppliedTo(final WCWInstance instance) {
			return true;
		}

		@Override
		public WCWInstance translate(final WCWInstance instance) {
			return new WCWInstance(instance.getContent()+(char)(((int) 'a')+RND.nextInt(26)));
		}
		
	}
	
	private class NewSpaceTranslator implements InstanceTranslator<WCWInstance> {

		@Override
		public boolean canBeAppliedTo(final WCWInstance instance) {
			return !instance.getContent().endsWith(" ");
		}

		@Override
		public WCWInstance translate(final WCWInstance instance) {
			return new WCWInstance(instance.getContent()+" ");
		}
		
	}
	
}
```

### generating some instances

Let's plug our new module to RUBENS! Go back to the root of the project and add our module in root `pom.xml` for it to be compiled using maven:

```xml
<modules>
        <module>fr.cril.rubens.core</module>
        <module>fr.cril.rubens.cnf</module>
        <module>fr.cril.rubens.arg</module>
        <module>fr.cril.rubens.wcw</module>
        <module>fr.cril.rubens.generator</module>
        <module>fr.cril.rubens.checker</module>
</modules>
```

Then, register our module in `fr.cril.rubens.generator/pom.xml` for it to be seen by the generator:
```xml
<dependencies>
        [...]
        <dependency>
                <groupId>${project.parent.groupId}</groupId>
                <artifactId>fr.cril.rubens.wcw</artifactId>
                <version>${project.parent.version}</version>
        </dependency>
        [...]
<dependencies>

```

Now, run `mvn package` to run the compilation process, then `java -jar fr.cril.rubens.generator/target/rubens-generator-0.1-SNAPSHOT.jar -l`, which displays the available instance generators. The output should contain the line 

```[INFO ] RUBENS-GEN: available methods (no family): WCW```

indicating our new instance generator can be used.

We can finally generate some instances in a new directory named `wcw` using the command

```java -jar fr.cril.rubens.generator/target/rubens-generator-0.1-SNAPSHOT.jar -m WCW -o wcw/```

The directory `wcw` has been created by RUBENS and contains some test cases (`XXX.txt`) and their corresponding number of words (`XXX.n`). One can generate more test cases by using the `-d` option (which controls the depth of the generation tree).


### Checking a software directly: the RUBENS checker

Instead of generating tests and check the behavior of a software using external tools, one can use RUBENS directly for tests. This way, RUBENS only exports instances (and expected results) for which the software result was unexpected.

To do this, a class implementing the `fr.cril.rubens.specs.TestGeneratorFactory` is needed. This interface defines 5 functions and must be named by a `ReflectorParam` annotation:

* at this time, ignore `getOptions` and `ignoreInstance`; make them return an empty list and `false`;
* `newTestGenerator` must return a test generator; return an instance of our new `WCWTestGenerator`;
* `checkSoftwareOutput` takes as input an instance and the solver output; depending of the correctness of the result, it returns a `CheckResult` object equal to `CheckResult.SUCCESS` or one built by `CheckResult.newError`
* finally, `newExecutor` is used to get an object used to launch the software under test on a single instance.

Most of the time, you just need to override the `fr.cril.rubens.utils.ASoftwareExecutor` class to build a software executor. This class has a single abstract method to be overriden, taking as input the location of the software under test, a mapping from file extensions to the paths of the files of the current instance, and the instance itself. This method must simply return the list of the command line arguments used to execute the software on the instance. Note that the software executor must also call its super constructor while creation (see the code below).


```java
@ReflectorParam(name="WCW-checker")
public class WCWCheckerFactory implements CheckerFactory<WCWInstance> {

	@Override
	public TestGeneratorFactory<WCWInstance> newTestGenerator() {
		return new WCWTestGenerator();
	}

	@Override
	public ASoftwareExecutor<WCWInstance> newExecutor(final Path execPath) {
		return new WCWExecutor(execPath);
	}

	@Override
	public CheckResult checkSoftwareOutput(final WCWInstance instance, final String result) {
		final long expected = instance.wordCount();
		final String trimmed = result.trim();
		try {
			if(Long.valueOf(trimmed).equals(expected)) {
				return CheckResult.SUCCESS;
			}
		} catch(NumberFormatException e) {
			return CheckResult.newError("expected a number, found \""+result+"\"");
		}
		return CheckResult.newError("expected "+expected+", found "+trimmed);
	}

	@Override
	public List<MethodOption> getOptions() {
		return Collections.emptyList();
	}

	@Override
	public boolean ignoreInstance(final WCWInstance instance) {
		return false;
	}
	
	private class WCWExecutor extends ASoftwareExecutor<WCWInstance> {
		
		public WCWExecutor(final Path execPath) {
			super(execPath);
		}

		@Override
		protected List<String> cliArgs(final Path execLocation, final Map<String, Path> instanceFiles, final WCWInstance instance) {
			return Stream.of(execLocation.toAbsolutePath().toString(), instanceFiles.get(".txt").toAbsolutePath().toString()).collect(Collectors.toList());
		}
		
	}

}
```

Just like we did for the instance generator, we need to reference our WCW module in `fr.cril.rubens.checker/pom.xml` (with a `dependency` tag) and recompile all the modules (`mvn package`). Now, executing `java -jar fr.cril.rubens.checker/target/rubens-checker-0.1-SNAPSHOT.jar -l` should output the line

```[INFO ] RUBENS-CHK: available methods (no family): WCW-checker```

This line indicates our checker is available. Let's use it on a software named `wcw.sh` located in the current directory, writing the test cases for which answers were incorrect in the `err/` directory.

```
java -jar fr.cril.rubens.checker/target/rubens-checker-0.1-SNAPSHOT.jar -m WCW-checker -o err -e ./wcw.sh
```

In case no errors are found, the output should look like this (and the `err/` directory is empty).

```
[INFO ] RUBENS-CHK: checking WCW-checker
[INFO ] RUBENS-CHK: checked 231 instances in 0,240s
[INFO ] RUBENS-CHK: found 0 errors.
[INFO ] RUBENS-CHK: ignored 0 instances.
```

In case some errors are detected, the output should looks like this one (and the `err/` directory should contain the instances and their expected results for which the software failed).

```
[INFO ] RUBENS-CHK: checking WCW-checker
[ERROR] RUBENS-CHK: WCW-checker error (1) for instance fr.cril.rubens.wcw.WCWInstance@2669b199: expected 5, found 4.
[ERROR] RUBENS-CHK: WCW-checker error (2) for instance fr.cril.rubens.wcw.WCWInstance@6fb554cc: expected a number, found "foo".
[INFO ] RUBENS-CHK: checked 231 instances in 0,272s
[INFO ] RUBENS-CHK: found 2 errors.
[INFO ] RUBENS-CHK: ignored 0 instances.
```

Note that successive calls to RUBENS will erase from `err/` the files which extensions correspond to the ones of the current call (except if no errors are detected). Note also that the instance description in the log (e.g. `fr.cril.rubens.wcw.WCWInstance@2669b199`) may be changed by overridden the `toString` method of the instance class.


## RUBENS Reflection process

RUBENS uses reflection to seek test generator and checker factories. It first looks for the class implementing `TestGeneratorFactory` and `CheckerFactory`, and then focus on their `ReflectorParam` annotation. They are some requirements on this annotation:

* it must be present;
* it must define a name (`name=foo`) or disable the class (`enable=false`), but not both;
* each name must be unique for a given interface.

Some other capabilities are provided (family names, collection of checkers, ...). See javadoc for more information.

## License

RUBENS is developed at CRIL (Centre de Recherche en Informatique de Lens) as a part of other projects. It is made available under the terms of the GNU GPLv3 license.
