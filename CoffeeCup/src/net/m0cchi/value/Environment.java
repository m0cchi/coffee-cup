package net.m0cchi.value;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	private final Map<String, Value> variableMap;
	private final Map<String, Function> functionMap;
	private Environment parent;

	public Environment() {
		variableMap = new HashMap<>();
		functionMap = new HashMap<>();
	}

	public Environment(Environment environment) {
		this();
		this.parent = environment;
	}

	public void defineVariable(String name, Value value) {
		this.variableMap.put(name, value);
	}

	public Value getValue(String name) {
		return this.variableMap.containsKey(name) ? this.variableMap.get(name) : this.parent != null ? this.parent.getValue(name) : new SList();
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
	
}
