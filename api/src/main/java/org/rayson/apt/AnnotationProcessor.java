/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.apt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.json.JsonException;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.rayson.api.Protocol;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * Annotation processor used to process Rayson Protocol service class.<br>
 * Notes, we can debug this processor by running javac as the following command
 * line shows:
 * 
 * <pre>
 * <b>
 * javac -processorpath Rayson-apt.jar -processor org.rayson.tool.apt.AnnotationProcessor \
 *  -J-Xdebug -J-Xrunjdwp:transport=dt_socket,address=9999,server=y,suspend=y Test.java
 * </b>
 * </pre>
 * 
 * Then, we can debug this processor by debugging remote java application on
 * Port 9999 in the IDE.
 * 
 * @author creativor
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({ AnnotationProcessor.TARGET_ANNOTATION_NAME })
public class AnnotationProcessor extends AbstractProcessor {
	private class MyTreeVistor extends SimpleTreeVisitor<Void, Void> {

		private static final String SERIALIZED_ANNOTATION_NAME = "SerializedName";
		private static final String API_ANNOTATION_PACKAGE_NMAE = "org.rayson.api.annotation";

		@Override
		public Void visitClass(ClassTree node, Void p) {
			List<? extends Tree> members = node.getMembers();
			for (Tree jcTree : members) {
				jcTree.accept(this, p);
			}
			return null;
		}

		@Override
		public Void visitMethod(MethodTree node, Void p) {
			for (VariableTree param : node.getParameters()) {
				param.accept(this, p);
			}
			return null;
		}

		@Override
		public Void visitVariable(VariableTree node, Void p) {
			// 1. Test  whether the annotation exists.
			if (JavacUtil.exitsAnnotation(node, API_ANNOTATION_PACKAGE_NMAE + "." + SERIALIZED_ANNOTATION_NAME))
				return null;
			// 2. Then add the annotation to the tree.
			Name packageName = names.fromString(API_ANNOTATION_PACKAGE_NMAE);
			Name annoTypeName = names.fromString(SERIALIZED_ANNOTATION_NAME);
			JCFieldAccess annType = treeMaker.Select(treeMaker.Ident(packageName), annoTypeName);
			String serializedName = node.getName().toString();
			JCExpression arg = treeMaker.Literal(serializedName);
			com.sun.tools.javac.util.List<JCExpression> args = com.sun.tools.javac.util.List.of(arg);
			// Visit method parameter here.
			JCAnnotation anno = treeMaker.Annotation(annType, args);
			JCModifiers mods = (JCModifiers) node.getModifiers();
			mods.annotations = mods.annotations.append(anno);
			return null;
		}

	}

	private final static Class<Protocol> PROTOCOL_CLASS = Protocol.class;

	final static String TARGET_ANNOTATION_NAME = "org.rayson.api.annotation.Service";

	private static String getErrorInfoFromException(final Throwable e) {
		try {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (final Exception e2) {
			return "bad getErrorInfoFromException";
		}
	}

	private Trees trees;

	private TreeMaker treeMaker;

	private Names names;

	private void copyJavaFile(final TypeElement protocolType) throws IOException {

		final String packageName = ((QualifiedNameable) protocolType.getEnclosingElement()).getQualifiedName().toString();
		final String fileName = protocolType.getSimpleName() + ".java";

		final FileObject fo = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, packageName, fileName);
		final Writer writer = fo.openWriter();
		this.processingEnv.getElementUtils().printElements(writer, protocolType);
		writer.close();
	}

	private void generateMetaData(final TypeElement protocolType) throws IOException, JsonException {
		
		final MetaDataParser parser = new MetaDataParser(protocolType, this.processingEnv);
		final String packageName = parser.getPackageName();

		final CharSequence fileName = parser.getFileName();
		final FileObject fo = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, packageName, fileName);
		// .createSourceFile(packageName + "." + metaClassName);
		final Writer writer = fo.openWriter();

		writer.append(parser.generateMetaData());
		writer.close();
	}

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {

		processingEnv.getMessager().printMessage(Kind.NOTE, "Init APT " + this.getClass().getName() + " ...");
		super.init(processingEnv);

		trees = Trees.instance(processingEnv);
		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
		treeMaker = TreeMaker.instance(context);
		names = Names.instance(context);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		Set<? extends Element> annotatedTypes;
		for (final TypeElement typeElement : annotations) {
			annotatedTypes = roundEnv.getElementsAnnotatedWith(typeElement);
			for (final Element element : annotatedTypes) {
				this.processingEnv.getMessager().printMessage(Kind.NOTE, "Found service annotated element -> " + element);
				processProtocol(element);
			}
		}

		return true;
	}

	private void processProtocol(final Element element) {
		final TypeMirror elementType = element.asType();

		Tree tree = trees.getTree(element);

		MyTreeVistor visitor = new MyTreeVistor();
		tree.accept(visitor, null);


		final ElementKind kind = element.getKind();
		final TypeKind typeKind = elementType.getKind();
		if (typeKind != TypeKind.DECLARED || !kind.isInterface()) {
			this.processingEnv.getMessager().printMessage(Kind.WARNING, "The Service annotated type should be an interface");
			return;
		}

		final TypeElement trueElem = (TypeElement) element;

		// Validate Protocol interface.
		final List<? extends TypeMirror> interfaces = trueElem.getInterfaces();
		boolean isProtocol = false;
		for (final TypeMirror interfaceType : interfaces) {
			if (interfaceType.toString().equals(PROTOCOL_CLASS.getName())) {
				isProtocol = true;
				break;
			}
		}
		if (!isProtocol) {
			this.processingEnv.getMessager().printMessage(Kind.WARNING, "The Service annotated type should be an child class of " + PROTOCOL_CLASS);
			return;

		}

		// Copy java file to class output directory.
		try {
			copyJavaFile(trueElem);
		} catch (final Throwable e) {
			this.processingEnv.getMessager().printMessage(Kind.WARNING, "Copy protocol java file failed:\n" + getErrorInfoFromException(e), trueElem);
		}

		// Do generate meta data of Protocol Interface.
		try {
			generateMetaData(trueElem);
		} catch (final Throwable e) {
			this.processingEnv.getMessager().printMessage(Kind.WARNING, "Generate meta data failed :\n" + getErrorInfoFromException(e), trueElem);
		}

	}

}