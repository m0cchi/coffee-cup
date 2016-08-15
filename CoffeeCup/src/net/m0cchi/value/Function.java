package net.m0cchi.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;

public abstract class Function extends Element implements Serializable {
	private static final long serialVersionUID = -6360469178584951813L;
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

	protected void defineVariable(Environment environment, String name, Element value) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		value = semanticAnalyzer.evaluate(value);
		environment.defineVariable(name, value);
	}

	protected void defineVariable(Environment environment, String name, List<Element> list) {
		List<Element> args = new ArrayList<>();
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		for (Element arg : list) {
			args.add(semanticAnalyzer.evaluate(arg));
		}
		environment.defineVariable(name, new SList(args));
	}

	public Element invoke(Environment environment, Element[] args) {
		Environment env = new Environment(environment);
		Iterator<String> parametor = Arrays.asList(this.args).iterator();
		Iterator<Element> argument = Arrays.asList(args).iterator();
		while (parametor.hasNext()) {
			String arg = parametor.next();
			if (arg.equals(REST)) {
				arg = parametor.next();
				List<Element> list = new ArrayList<>();
				while (argument.hasNext()) {
					list.add(argument.next());
				}
				defineVariable(env, arg, list);
			} else {
				Element element = argument.next();
				defineVariable(env, arg, element);
			}
		}

		// hook
		hook(env);

		return invoke(env);
	}

	public void hook(Environment env) {
		// nop
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
