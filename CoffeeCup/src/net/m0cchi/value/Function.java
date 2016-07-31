package net.m0cchi.value;

import net.m0cchi.parser.semantic.SemanticAnalyzer;

public abstract class Function extends Value {
	private final static String[] NONE_ARGS = {};
	private String[] args;

	public Function() {
		this.type = AtomicType.FUNCTION;
		this.args = NONE_ARGS;
	}

	public abstract Value invoke(Environment environment);

	public void defineVariable(Environment environment, String name, Value value) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		value = semanticAnalyzer.evaluate(value);
		environment.defineVariable(name, value);
	}

	public Value invoke(Environment environment, Value[] args) {
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
