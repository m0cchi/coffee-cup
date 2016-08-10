package net.m0cchi.value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.m0cchi.value.NULL.NIL;

public class Environment implements Serializable {
	private static final long serialVersionUID = -586360650485649973L;
	protected static final Map<String, Environment> PACKAGES = new ConcurrentHashMap<>();
	private Map<String, Element> variableMap;
	private Map<String, Function> functionMap;
	private Environment parent;

	public Environment() {
		variableMap = new HashMap<>();
		functionMap = new HashMap<>();
		variableMap.put("nil", new NIL());
	}

	public Environment(Environment environment) {
		this();
		this.parent = environment;
	}

	public static boolean hasPackage(String name) {
		return PACKAGES.containsKey(name);
	}

	public static void addEnvironment(String name, Environment environment) {
		PACKAGES.putIfAbsent(name, environment);
	}

	public void naming(String name) {
		PACKAGES.putIfAbsent(name, this);
	}

	public void load(String name) {
		Environment environment = PACKAGES.get(name);
		this.variableMap = environment.variableMap;
		this.functionMap = environment.functionMap;
		this.parent = environment.parent;
	}

	public void defineVariable(String name, Element element) {
		this.variableMap.put(name, element);
	}

	public Element getValue(String name) {
		return this.variableMap.containsKey(name) ? this.variableMap.get(name) : this.parent != null ? this.parent.getValue(name) : new SList();
	}

	public void setValue(String name, Element value) {
		Environment pointer = this;
		do {
			if (pointer.variableMap.containsKey(name)) {
				break;
			}
		} while ((pointer = pointer.getParent()) != null);

		if (pointer == null) {
			pointer = this;
		}

		pointer.variableMap.put(name, value);
	}

	public void defineFunction(String name, Function function) {
		this.functionMap.put(name, function);
	}

	public Function getFunction(String name) {
		return this.functionMap.containsKey(name) ? this.functionMap.get(name) : this.parent != null ? this.parent.getFunction(name) : null;
	}

	public Environment getParent() {
		return parent;
	}

	public void setParent(Environment parent) {
		this.parent = parent;
	}

	public ClassLoader getClassLoader() {
		// TODO: impl package
		return Environment.class.getClassLoader();
	}

}
