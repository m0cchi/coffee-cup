package net.m0cchi.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;

public abstract class Function extends Element {
	private final static String[] NONE_ARGS = {};
	protected final static String REST = "&rest";
	private String[] args;

	{
		init();
		this.type = AtomicType.FUNCTION;
	}

	protected final void init() {
		this.args = NONE_ARGS;
	}

	public abstract Element invoke(Environment environment);

	public void defineVariable(Environment environment, String name, Element value) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		value = semanticAnalyzer.evaluate(value);
		environment.defineVariable(name, value);
	}

	public Element invoke(Environment environment, Element[] args) {
		Environment env = new Environment(environment);
		Iterator<String> parametor = Arrays.asList(this.args).iterator();
		Iterator<Element> argument = Arrays.asList(args).iterator();
		while (parametor.hasNext()) {
			String arg = parametor.next();
			Element element = null;
			if (arg.equals(REST)) {
				arg = parametor.next();
				List<Element> list = new ArrayList<>();
				element = new SList(list);
				while (argument.hasNext()) {
					list.add(argument.next());
				}
			} else {
				element = argument.next();
			}

			defineVariable(env, arg, element);
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
