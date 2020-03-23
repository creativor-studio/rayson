/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.apt;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AnnotationProcessorTest {
	@Test
	public void runAnnoationProcessor() throws Exception {

		java.util.List<String> options = new ArrayList<>();
		// set compiler's classpath to be same as the runtime's
		Collections.addAll(options, "-d", Files.createTempDirectory("apt").toString(), "-classpath", System.getProperty("java.class.path"));

		options.add("--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED");
		options.add("--add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED");
		options.add("--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED");
		options.add("--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED");

		String source = "src/test/java";

		Iterable<JavaFileObject> files = getSourceFiles(source);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, null, options, null, files);
		task.setProcessors(List.of(new AnnotationProcessor()));
		// task.setProcessors(List.of((Processor)
		// Class.forName("lombok.launch.AnnotationProcessorHider$AnnotationProcessor").newInstance()));
		task.call();
	}

	private Iterable<JavaFileObject> getSourceFiles(String p_path) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

		files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(p_path)));

		Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
		return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//System.setProperty("java.home", "/Library/Java/JavaVirtualMachines/jdk-11.0.5.jdk/Contents/Home");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
}
