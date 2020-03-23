/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.apt;

import java.util.List;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.Name;

/**
 * Javac compiler utility for APT.
 * 
 * @author creativor
 *
 */
final class JavacUtil {
	/**
	 * Test whether the given annotation exists in node's modifiers.
	 * 
	 * @param node Method parameter node.
	 * @param annoName Annotation name.
	 * @return True if exists such annotation.Or else, <code>false</code>.
	 */
	public static boolean exitsAnnotation(VariableTree node, String annoName) {
		List<? extends AnnotationTree> annos = node.getModifiers().getAnnotations();
		if (annos == null || annos.isEmpty())
			return false;
		for (AnnotationTree anno : annos) {
			Tree annoType = anno.getAnnotationType();
			if (!(annoType instanceof JCIdent))
				continue;
			JCIdent realAnnoType = (JCIdent) annoType;
			Name qName = realAnnoType.sym.getQualifiedName();
			boolean equals = qName.contentEquals(annoName);
			if (equals)
				return true;

		}
		return false;
	}

	private JavacUtil() {
		// Forbidden
	}

}
