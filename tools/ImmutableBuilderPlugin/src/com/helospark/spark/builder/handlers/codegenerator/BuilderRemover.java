
package com.helospark.spark.builder.handlers.codegenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * Removes the builder if exists.
 * 
 * @author helospark
 */
public class BuilderRemover {

	@SuppressWarnings("unchecked")
	public void removeExistingBuilder(AST ast, ASTRewrite rewriter, TypeDeclaration builderTypeDeclaration, TypeDeclaration mainType,
			MethodDeclaration builderMethod) {

		Optional<MethodDeclaration> constructorToRemove = findPrivateConstructor(builderTypeDeclaration, mainType);
		if (constructorToRemove.isPresent())
			rewriter.remove(constructorToRemove.get(), null);

		rewriter.remove(builderMethod, null);
		rewriter.remove(builderTypeDeclaration, null);

	}

	@SuppressWarnings("unchecked")
	public Optional<MethodDeclaration> findBuilderMethod(TypeDeclaration builderTypeDeclaration, TypeDeclaration mainType) {
		return Arrays.stream(mainType.getMethods()).filter(method -> !method.isConstructor()).filter(method -> isStatic(method.modifiers()))
				.filter(method -> method.parameters().size() == 0).filter(method -> isReturnTypeSameAsBuilder(builderTypeDeclaration, method.getReturnType2()))
				.findFirst();
	}

	private Optional<MethodDeclaration> findPrivateConstructor(TypeDeclaration builderTypeDeclaration, TypeDeclaration mainType) {
		return Arrays.stream(mainType.getMethods()).filter(method -> method.isConstructor()).filter(method -> method.parameters().size() == 0).findFirst();
	}

	private boolean isStatic(List<IExtendedModifier> list) {
		return list.stream().filter(element -> element instanceof Modifier)
				.filter(element -> ((Modifier) element).getKeyword() == ModifierKeyword.STATIC_KEYWORD).findFirst().map(element -> Boolean.TRUE)
				.orElse(Boolean.FALSE);
	}

	private boolean isReturnTypeSameAsBuilder(TypeDeclaration builderTypeDeclaration, Type type) {
		return type.toString().equals(getBuilderTypeName(builderTypeDeclaration));
	}

	private String getBuilderTypeName(TypeDeclaration builderTypeDeclaration) {
		return builderTypeDeclaration.getName().toString();
	}

}
