package net.m0cchi.value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.NULL.NIL;

public abstract class Function extends Element implements Serializable {
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
		boolean defaultNil = false;
		while (parametor.hasNext()) {
			String arg = parametor.next();
			if(arg.equals(DEFAULT_NIL)){
				defaultNil = true;
			} else if (arg.equals(REST)) {
				arg = parametor.next();
				List<Element> list = new ArrayList<>();
				while (argument.hasNext()) {
					list.add(argument.next());
				}
				defineVariable(env, arg, list);
			} else {
				try {
					Element element = argument.next();
					defineVariable(env, arg, element);
				} catch (NoSuchElementException e) {
					if(defaultNil) {
						defineVariable(env, arg, NIL.NIL);
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

	public String getName() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<Integer> points = new ArrayList<>();
		String orignString = this.getClass().getSimpleName();
		byte[] origin = orignString.getBytes();
		int A = 65;
		int Z = 90;
		int tolowerValue = 32;// a - A
		for (int i = 0; i < origin.length; i++) {
			byte ch = origin[i];
			if (ch >= A && ch <= Z) {
				points.add(i);
				origin[i] = (byte) (origin[i] + tolowerValue);
			}
		}

		if (points.size() == 0 || points.size() == 1) {
			return new String(origin);
		}

		points.remove(0);

		if ("def".equalsIgnoreCase(new String(origin, 0, points.get(0)))) {
			points.remove(0);
			if (points.size() == 0) {
				return new String(origin);
			}
		}

		int before = 0;
		Iterator<Integer> it = points.iterator();
		while (it.hasNext()) {
			baos.write(origin, before, (-before + (before += it.next())));
			if (it.hasNext()) {
				baos.write('-');
			} else {
				baos.write('-');
				baos.write(origin, before, origin.length - before);
			}
		}

		String ret = baos.toString();
		try {
			baos.close();
		} catch (IOException e) {
		}

		return ret;
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
