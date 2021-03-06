package net.m0cchi.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.NULL.NIL;
import net.m0cchi.value.i.Namable;

public abstract class Function extends Element implements Serializable, Namable {
	private static final long serialVersionUID = -6360469178584951813L;
	private final static String[] NONE_ARGS = {};
	protected final static String REST = "&rest";
	protected final static String DEFAULT_NIL = "&default-nil";
	private String[] args;

	{
		init();
		this.type = AtomicType.FUNCTION;
	}

	protected final void init() {
		this.args = NONE_ARGS;
	}

	public abstract Element invoke(Environment environment);

	protected void defineVariable(Environment ref, Environment environment, String name, Element value) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(ref);
		value = semanticAnalyzer.evaluate(value);
		environment.defineVariable(name, value);
	}

	protected void defineVariable(Environment ref, Environment environment, String name, List<Element> list) {
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
		boolean defaultNil = false;
		while (parametor.hasNext()) {
			String arg = parametor.next();
			if (arg.equals(DEFAULT_NIL)) {
				defaultNil = true;
			} else if (arg.equals(REST)) {
				arg = parametor.next();
				List<Element> list = new ArrayList<>();
				while (argument.hasNext()) {
					list.add(argument.next());
				}
				defineVariable(environment, env, arg, list);
			} else {
				try {
					Element element = argument.next();
					defineVariable(environment, env, arg, element);
				} catch (NoSuchElementException e) {
					if (defaultNil) {
						defineVariable(environment, env, arg, NIL.NIL);
					} else {
						RuntimeException exception = new Abort();
						exception.setStackTrace(e.getStackTrace());
						throw e;
					}
				}
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

	public void setArgs(String... args) {
		this.args = args;
	}

	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(args);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (!Arrays.equals(args, other.args))
			return false;
		return true;
	}

}
