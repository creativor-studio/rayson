
package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDER_CLASS_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_CLASS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Generates the builder class.
 * 
 * @author helospark
 */
@SuppressWarnings("unchecked")
public class BuilderClassCreator {

	private static final String BUILDER_WRAPPED_FIELD_NAME = "wrapped";
	private static final String CLASS_NAME_REPLACEMENT_PATTERN = "className";
	private BuilderMethodNameBuilder builderClassMethodNameGeneratorService;
	private TemplateResolver templateResolver;
	private PreferencesManager preferencesManager;
	private JavadocGenerator javadocGenerator;
	private NonNullAnnotationAttacher nonNullAnnotationAttacher;
	private GeneratedAnnotationPopulator generatedAnnotationPopulator;

	public BuilderClassCreator(BuilderMethodNameBuilder builderClassMethodNameGeneratorService, TemplateResolver templateResolver,
			PreferencesManager preferencesManager, JavadocGenerator javadocGenerator, NonNullAnnotationAttacher nonNullAnnotationAttacher,
			GeneratedAnnotationPopulator generatedAnnotationPopulator) {
		this.builderClassMethodNameGeneratorService = builderClassMethodNameGeneratorService;
		this.templateResolver = templateResolver;
		this.preferencesManager = preferencesManager;
		this.javadocGenerator = javadocGenerator;
		this.nonNullAnnotationAttacher = nonNullAnnotationAttacher;
		this.generatedAnnotationPopulator = generatedAnnotationPopulator;
	}

	public TypeDeclaration createBuilderClass(AST ast, TypeDeclaration originalName, List<NamedVariableDeclarationField> namedVariableDeclarations) {
		TypeDeclaration builderType = createBuilderClass(ast, originalName);
		addPrivateField(ast, builderType, originalName);
		addPrivateConstructor(ast, builderType, originalName);
		for (NamedVariableDeclarationField namedVariableDeclarationField : namedVariableDeclarations) {
			// addFieldToBuilder(ast, builderType,
			// namedVariableDeclarationField);
			addWithMethodToBuilder(ast, builderType, namedVariableDeclarationField);
		}
		addBuildMethodToBuilder(ast, originalName, builderType);
		return builderType;
	}

	private void addPrivateField(AST ast, TypeDeclaration builderType, TypeDeclaration originalName) {
		VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(ast.newSimpleName(BUILDER_WRAPPED_FIELD_NAME));
		FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
		fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
		fieldDeclaration.setType(ast.newSimpleType(ast.newName(originalName.getName().toString())));
		builderType.bodyDeclarations().add(fieldDeclaration);
	}

	private void addPrivateConstructor(AST ast, TypeDeclaration builderType, TypeDeclaration originalName) {
		MethodDeclaration privateConstructorMethod = ast.newMethodDeclaration();
		Block block = ast.newBlock();
		Assignment expression = ast.newAssignment();
		FieldAccess fieldAccess = ast.newFieldAccess();
		fieldAccess.setExpression(ast.newThisExpression());
		fieldAccess.setName(ast.newSimpleName(BUILDER_WRAPPED_FIELD_NAME));
		expression.setLeftHandSide(fieldAccess);
		ClassInstanceCreation classCreation = ast.newClassInstanceCreation();
		classCreation.setType(ast.newSimpleType(ast.newName(originalName.getName().toString())));
		expression.setRightHandSide(classCreation);
		Object statement = ast.newExpressionStatement(expression);
		block.statements().add(statement);
		privateConstructorMethod.setBody(block);
		privateConstructorMethod.setConstructor(true);
		privateConstructorMethod.setName(ast.newSimpleName(builderType.getName().toString()));
		privateConstructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
		builderType.bodyDeclarations().add(privateConstructorMethod);
	}

	// private void addFieldToBuilder(AST ast, TypeDeclaration newType,
	// NamedVariableDeclarationField namedVariableDeclarationField) {
	// VariableDeclarationFragment variableDeclarationFragment =
	// ast.newVariableDeclarationFragment();
	// variableDeclarationFragment.setName(ast.newSimpleName(namedVariableDeclarationField.getFieldName()));
	// FieldDeclaration fieldDeclaration =
	// ast.newFieldDeclaration(variableDeclarationFragment);
	// fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
	// fieldDeclaration.setType((Type) ASTNode.copySubtree(ast,
	// namedVariableDeclarationField.getFieldDeclaration().getType()));
	// newType.bodyDeclarations().add(findLastFieldIndex(newType),
	// fieldDeclaration);
	// }

	// private int findLastFieldIndex(TypeDeclaration newType) {
	// return ((List<BodyDeclaration>)
	// newType.bodyDeclarations()).stream().filter(element -> element instanceof
	// FieldDeclaration).collect(Collectors.toList())
	// .size();
	// }

	private void addWithMethodToBuilder(AST ast, TypeDeclaration newType, NamedVariableDeclarationField namedVariableDeclarationField) {
		String fieldName = namedVariableDeclarationField.getFieldName();
		Block newBlock = createWithMethodBody(ast, fieldName);
		SingleVariableDeclaration methodParameterDeclaration = createMethodParameter(ast, namedVariableDeclarationField.getFieldDeclaration(), fieldName);
		MethodDeclaration newMethod = createNewWithMethod(ast, fieldName, newBlock, methodParameterDeclaration, newType);
		newType.bodyDeclarations().add(newMethod);
	}

	private Block createWithMethodBody(AST ast, String fieldName) {
		Block newBlock = ast.newBlock();
		ReturnStatement builderReturnStatement = ast.newReturnStatement();
		builderReturnStatement.setExpression(ast.newThisExpression());

		Assignment newAssignment = ast.newAssignment();

		newAssignment.setLeftHandSide(ast.newName(new String[] { BUILDER_WRAPPED_FIELD_NAME, fieldName }));
		newAssignment.setRightHandSide(ast.newSimpleName(fieldName));

		newBlock.statements().add(ast.newExpressionStatement(newAssignment));
		newBlock.statements().add(builderReturnStatement);
		return newBlock;
	}

	private MethodDeclaration createNewWithMethod(AST ast, String fieldName, Block newBlock, SingleVariableDeclaration methodParameterDeclaration,
			TypeDeclaration newType) {
		MethodDeclaration builderMethod = ast.newMethodDeclaration();
		builderMethod.setName(ast.newSimpleName(builderClassMethodNameGeneratorService.build(fieldName)));
		builderMethod.setReturnType2(ast.newSimpleType(ast.newName(newType.getName().toString())));
		builderMethod.setBody(newBlock);
		builderMethod.parameters().add(methodParameterDeclaration);

		if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
			Map<String, String> tags = new HashMap<>();
			tags.put(RETURN_JAVADOC_TAG_NAME, "The builder itself.");
			tags.put("@param", fieldName + " build field " + fieldName + " .");
			Javadoc javadoc = javadocGenerator.generateJavadoc(ast, String.format(Locale.ENGLISH, "Builder method for %s parameter.", fieldName), tags);
			builderMethod.setJavadoc(javadoc);
		}
		if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
			nonNullAnnotationAttacher.attachNonNull(ast, builderMethod);
		}
		builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		return builderMethod;
	}

	private SingleVariableDeclaration createMethodParameter(AST ast, FieldDeclaration field, String fieldName) {
		SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
		methodParameterDeclaration.setType((Type) ASTNode.copySubtree(ast, field.getType()));
		methodParameterDeclaration.setName(ast.newSimpleName(fieldName));
		if (preferencesManager.getPreferenceValue(PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS)) {
			nonNullAnnotationAttacher.attachNonNull(ast, methodParameterDeclaration);
		}
		return methodParameterDeclaration;
	}

	private void addBuildMethodToBuilder(AST ast, TypeDeclaration typeDecl, TypeDeclaration newType) {
		MethodDeclaration buildMethod = createBuildMethod(ast, newType, typeDecl);
		newType.bodyDeclarations().add(buildMethod);
	}

	private MethodDeclaration createBuildMethod(AST ast, TypeDeclaration newType, TypeDeclaration originalType) {
		ReturnStatement statement = ast.newReturnStatement();
		statement.setExpression(ast.newSimpleName(BUILDER_WRAPPED_FIELD_NAME));

		Block block = ast.newBlock();
		block.statements().add(statement);

		MethodDeclaration method = ast.newMethodDeclaration();
		method.setName(ast.newSimpleName(getBuildMethodName(originalType)));
		method.setBody(block);
		method.setReturnType2(ast.newSimpleType(ast.newName(originalType.getName().toString())));

		if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
			Javadoc javadoc = javadocGenerator.generateJavadoc(ast, "Builder method of the builder.",
					Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "built class"));
			method.setJavadoc(javadoc);
		}
		if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
			nonNullAnnotationAttacher.attachNonNull(ast, method);
		}

		method.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		return method;
	}

	private String getBuildMethodName(TypeDeclaration originalType) {
		Map<String, String> replacementMap = new HashMap<>();
		replacementMap.put(CLASS_NAME_REPLACEMENT_PATTERN, originalType.getName().toString());
		return templateResolver.resolveTemplate(preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN), replacementMap);
	}

	private TypeDeclaration createBuilderClass(AST ast, TypeDeclaration originalType) {
		TypeDeclaration builderType = ast.newTypeDeclaration();
		builderType.setName(ast.newSimpleName(getBuilderName(originalType)));

		if (preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION)) {
			generatedAnnotationPopulator.addGeneratedAnnotation(ast, builderType);
		}
		builderType.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		builderType.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		builderType.modifiers().add(ast.newModifier(ModifierKeyword.FINAL_KEYWORD));

		if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_BUILDER_CLASS)) {
			Javadoc javadoc = javadocGenerator.generateJavadoc(ast,
					String.format(Locale.ENGLISH, "Builder to build {@link %s}.", originalType.getName().toString()), Collections.emptyMap());
			builderType.setJavadoc(javadoc);
		}

		return builderType;
	}

	private String getBuilderName(TypeDeclaration originalType) {
		Map<String, String> replacementMap = new HashMap<>();
		replacementMap.put(CLASS_NAME_REPLACEMENT_PATTERN, originalType.getName().toString());
		return templateResolver.resolveTemplate(preferencesManager.getPreferenceValue(BUILDER_CLASS_NAME_PATTERN), replacementMap);
	}

}
