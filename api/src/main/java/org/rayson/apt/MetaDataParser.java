/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.apt;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.AbstractElementVisitor7;
import javax.tools.Diagnostic.Kind;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.DocumentedAnnotation;
import org.rayson.api.annotation.Nullable;
import org.rayson.api.protocol.info.AnnotationInfo;
import org.rayson.api.protocol.info.MethodInfo;
import org.rayson.api.protocol.info.ParameterInfo;
import org.rayson.api.protocol.info.ProtocolInfo;

/**
 * An helper class used to parse meta data of {@link Protocol} class source
 * code.
 * 
 * @author creativor
 */
final class MetaDataParser {

	private static class MethodDocument {
		private String comment;
		private String returnComment;
		private final Map<String, String> paramComments;

		public MethodDocument() {
			paramComments = new HashMap<>();
		}

		void addParamComment(final String param, final String paramComment) {
			paramComments.put(param, paramComment);
		}

		public String getParamComment(final String parameterName) {
			return paramComments.get(parameterName);
		}

		void setComment(final String comment) {
			this.comment = comment;
		}

		void setReturnComment(final String returnComment) {
			this.returnComment = returnComment;
		}
	}

	private final class MethodParser {
		private static final char AT_TOKEN = '@';
		// private static final char SPACE_TOKEN = ' ';
		private static final String RETURN_TAG = "@return";
		private static final String PARAM_TAG = "@param";
		private final ExecutableElement element;
		private String name;
		private String returnType;
		private final List<ParameterInfo> parameters;
		private final List<AnnotationInfo> annotations;

		public MethodParser(final ExecutableElement element) {
			this.element = element;
			parameters = new ArrayList<>();
			annotations = new ArrayList<>();
		}

		private String buildPart(final String[] lines, final int from, final int to) {
			final StringBuffer sb = new StringBuffer();
			for (int i = from; i <= to; i++) {
				sb.append(lines[i]);
				sb.append("\r\n");
			}
			return sb.toString();
		}

		public MethodInfo generateInfo() {

			// Basic info.
			this.name = element.getSimpleName().toString();

			returnType = element.getReturnType().toString();

			final MethodDocument document = parseDoc(processingEnv.getElementUtils().getDocComment(element));

			// Parameters info.
			final List<? extends VariableElement> params = element.getParameters();

			ParameterInfo paramInfo;
			String parameterName;
			String parameterComment;
			int paramIndex = 0;

			for (final VariableElement param : params) {
				parameterName = param.getSimpleName().toString();
				parameterComment = document.getParamComment(parameterName);
				paramInfo = new ParameterInfo(paramIndex++, parameterName, parameterComment);
				parameters.add(paramInfo);
			}

			// Annotations info.
			final List<? extends AnnotationMirror> annos = element.getAnnotationMirrors();

			AnnotationInfo annoInfo;
			for (final AnnotationMirror anno : annos) {
				if (containsDocumentedAnnotation(anno)) {
					annoInfo = new AnnotationInfo(anno.toString());
					annotations.add(annoInfo);
				}
			}

			return new MethodInfo(name, document.comment, this.returnType, document.returnComment, parameters, annotations);
		}

		private MethodDocument parseDoc(final String docComment) {
			final MethodDocument doc = new MethodDocument();
			if (docComment == null || docComment.isEmpty())
				return doc;

			final List<String> parts = new ArrayList<>();
			final String[] lines = docComment.split("\\r?\\n");
			int mark = 0;
			String previousPart;
			for (int i = 0; i < lines.length; i++) {
				final String line = lines[i].trim();
				if (line == null || line.isEmpty())
					continue;
				if (line.charAt(0) == AT_TOKEN) {
					// Build previous part.
					if (i == 0) // only tags.
						continue;
					previousPart = buildPart(lines, mark, i - 1);
					mark = i;
					parts.add(previousPart);
				}
			}

			// Build last part.
			final String lastPart = buildPart(lines, mark, lines.length - 1);

			parts.add(lastPart.trim());

			// Parse document parts.
			for (String part : parts) {
				if (part.charAt(0) == AT_TOKEN) {
					parseTag(doc, part);
				} else {
					doc.setComment(part);
				}
			}

			return doc;
		}

		private void parseTag(final MethodDocument doc, final String part) {

			if (part == null || part.isEmpty())
				return;

			final Pattern pattern = Pattern.compile("\\s+");

			final Matcher matcher = pattern.matcher(part);

			if (!matcher.find())
				return;

			int firstSpaceIndex = matcher.start();
			if (firstSpaceIndex == -1)
				return;

			final String tag = part.substring(0, firstSpaceIndex);
			switch (tag) {
			case RETURN_TAG:
				if (firstSpaceIndex >= part.length() - 1)
					return;
				final String returnComment = part.substring(matcher.end());
				doc.setReturnComment(returnComment.trim());
				break;
			case PARAM_TAG:
				firstSpaceIndex = matcher.end();
				if (!matcher.find())
					break;
				final int secondSpaceIndex = matcher.start();
				if (secondSpaceIndex == -1 || secondSpaceIndex >= part.length() - 1)
					return;
				final String param = part.substring(firstSpaceIndex, secondSpaceIndex);
				final String paramComment = part.substring(matcher.end());
				doc.addParamComment(param, paramComment.trim());
				break;
			default:
				break;
			}

		}
	}

	private class ProtocolVistor extends AbstractElementVisitor7<Void, Void> {

		private String name;
		private String comment;
		private final Set<MethodInfo> methods;

		public ProtocolVistor() {
			methods = new HashSet<>();
		}

		ProtocolInfo generateInfo() {
			return new ProtocolInfo(name, comment, methods);

		}

		@Override
		public Void visitExecutable(final ExecutableElement e, final Void p) {
			if (e.getKind() != ElementKind.METHOD)
				return null;

			processingEnv.getMessager().printMessage(Kind.NOTE, "visitExecutable method: " + e);
			final MethodParser methodVistor = new MethodParser(e);
			final MethodInfo methodInfo = methodVistor.generateInfo();
			this.methods.add(methodInfo);
			return null;
		}

		@Override
		public Void visitPackage(final PackageElement e, final Void p) {
			return null;
		}

		@Override
		public Void visitType(final TypeElement e, final Void p) {
			processingEnv.getMessager().printMessage(Kind.NOTE, "visitType: " + e);
			this.name = e.getQualifiedName().toString();
			this.comment = processingEnv.getElementUtils().getDocComment(e);
			final List<? extends Element> eles = e.getEnclosedElements();
			for (final Element ele : eles) {
				ele.accept(this, null);
			}
			return null;
		}

		@Override
		public Void visitTypeParameter(final TypeParameterElement e, final Void p) {
			processingEnv.getMessager().printMessage(Kind.NOTE, "visitTypeParameter: " + e);
			return null;
		}

		@Override
		public Void visitVariable(final VariableElement e, final Void p) {
			processingEnv.getMessager().printMessage(Kind.NOTE, "visitVariable: " + e);
			return null;
		}

	}

	private final static String META_DATA_CLASS_SUFFIX = "$rayson$proto";

	/**
	 * Test whether an giving {@link AnnotationMirror} type contains annotation
	 * {@link DocumentedAnnotation}.
	 * 
	 * @param am Source annotation.
	 * @return <code>true</code> if contains it. Or else, <code>false</code>.
	 */
	static boolean containsDocumentedAnnotation(final AnnotationMirror am) {
		if (am == null)
			return false;
		try {
			final List<? extends AnnotationMirror> ams = am.getAnnotationType().asElement().getAnnotationMirrors();
			for (final AnnotationMirror a : ams) {
				if (a.getAnnotationType().toString().equals(DocumentedAnnotation.class.getName()))
					return true;
			}
		} catch (final Exception e) {
			// Ignore it.
		}

		return false;
	}

	private final TypeElement protocolType;

	private final String fileName;
	private final String packageName;

	private final ProcessingEnvironment processingEnv;

	MetaDataParser(final TypeElement protocolType, final ProcessingEnvironment processingEnv) {
		this.protocolType = protocolType;
		this.processingEnv = processingEnv;
		this.fileName = protocolType.getSimpleName() + META_DATA_CLASS_SUFFIX + ".json";
		this.packageName = ((QualifiedNameable) this.protocolType.getEnclosingElement()).getQualifiedName().toString();
	}

	/**
	 * Generate meta data of this protocol class, in Json format.
	 * 
	 * @return Generated meta data in json string.
	 * @throws JsonException If failed to generate.
	 */
	String generateMetaData() throws JsonException {

		final ProtocolVistor v = new ProtocolVistor();

		protocolType.accept(v, null);

		return toJson(v.generateInfo());
	}

	private static String toJson(@Nullable final ProtocolInfo bean) throws JsonException {
		StringWriter writer = new StringWriter();
		Map<String, Object> properties = new HashMap<>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);
		JsonGenerator generator = factory.createGenerator(writer);
		bean.toJson(generator);
		generator.close();
		return writer.toString();

	}

	public CharSequence getFileName() {
		return this.fileName;
	}

	public String getPackageName() {
		return this.packageName;
	}
}