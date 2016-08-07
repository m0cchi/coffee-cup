package net.m0cchi.value;

import java.util.HashMap;
import java.util.Map;

import net.m0cchi.value.NULL.NIL;

public class Environment {
	private final Map<String, Element> variableMap;
	private final Map<String, Function> functionMap;
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
