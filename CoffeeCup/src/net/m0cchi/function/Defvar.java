package net.m0cchi.function;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Defvar extends Macro {
	private static final long serialVersionUID = -1571318218506031859L;

	public Defvar() {
		setArgs(new String[] { "def name", "def value" });
	}

	@Override
	public Element invoke(Environment environment) {
		Value<String> name = environment.getValue(getArgs()[0]);
		Value<String> value = environment.getValue(getArgs()[1]);
		Environment parent = environment.getParent();
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		parent.defineVariable(name.getNativeValue(), semanticAnalyzer.evaluate(value));
		return new SList();
	}

}
