package net.m0cchi.value;

import net.m0cchi.parser.semantic.SemanticAnalyzer;

public abstract class Function extends Element {
	private final static String[] NONE_ARGS = {};
	private String[] args;

	public Function() {
		this.type = AtomicType.FUNCTION;
		this.args = NONE_ARGS;
	}

	public abstract Element invoke(Environment environment);

	public void defineVariable(Environment environment, String name, Element value) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		value = semanticAnalyzer.evaluate(value);
		environment.defineVariable(name, value);
	}

	public Element invoke(Environment environment, Element[] args) {
		int point = 0;
		Environment env = new Environment(environment);
		for (String arg : this.args) {
			defineVariable(env, arg, args[point++]);
		}
		return invoke(env);
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
