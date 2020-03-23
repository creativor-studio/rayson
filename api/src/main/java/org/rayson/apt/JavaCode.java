/**
 * Copyright © 2020 Creativor Studio. About license information, please see
 * LICENSE.txt.
 */

package org.rayson.apt;

final class JavaCode {

	private static final String SPACE = " ";
	private static final String INDENT_STRING = "\t";
	private static final String PUBLIC_STATIC_FINAL = "public static final";
	private int indentSpace = 0;
	private final StringBuffer sb;

	JavaCode() {
		this.sb = new StringBuffer();
	}

	public void addPublicConstant(final String name, final Class<?> type, final Object value) {
		String code = PUBLIC_STATIC_FINAL;
		code += (SPACE + type.getName() + SPACE + name + " = " + value);
		this.appendCodeLine(code);
	}

	public void append(final JavaCode javaCode) {
		this.sb.append(javaCode.sb);
	}

	public void appendCodeLine(final String code) {
		fillIndent();
		this.sb.append(code);
		this.sb.append(";");
		newLine();
	}

	public void appendImport(final Class<?> importClass) {
		appendCodeLine("import " + importClass.getCanonicalName());
	}

	private void appendIndent() {
		this.sb.append(INDENT_STRING);
	}

	public void appendLine(final String code) {
		fillIndent();
		this.sb.append(code);
		newLine();
	}

	public void beginBracket() {
		appendLine("{");
		newLine();
		this.indentSpace += 1;
	}

	public void endBracket() {
		appendLine("}");
		newLine();
		this.indentSpace -= 1;
	}

	private void fillIndent() {
		if (this.indentSpace <= 0) {
			return;
		}
		for (int i = 0; i < this.indentSpace; i++) {
			appendIndent();
		}
	}

	public void newLine() {
		this.sb.append("\n");
	}

	@Override
	public String toString() {
		return this.sb.toString();
	}
}