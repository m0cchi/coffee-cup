package net.m0cchi.function;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Defvar extends Macro {

	public Defvar() {
		super();
		setArgs(new String[] { "def name", "def value" });
	}

	@Override
	public Value invoke(Environment environment) {
		AtomicValue name = (AtomicValue) environment.getValue(getArgs()[0]);
		AtomicValue value = (AtomicValue) environment.getValue(getArgs()[1]);
		Environment parent = environment.getParent();
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
		parent.defineVariable(name.getNativeValue(), semanticAnalyzer.evaluate(value));
		return new SList();
	}

}
