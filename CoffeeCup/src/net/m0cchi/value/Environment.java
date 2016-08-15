package net.m0cchi.value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.m0cchi.value.NULL.NIL;

public class Environment implements Serializable {
	private static final long serialVersionUID = -586360650485649973L;
	protected static final Map<String, Environment> PACKAGES = new ConcurrentHashMap<>();
	protected Map<String, Element> variableMap;
	protected Map<String, Function> functionMap;
	protected Environment parent;

	public Environment() {
		variableMap = new HashMap<>();
		functionMap = new HashMap<>();
		variableMap.put("nil", new NIL());
	}

	public Environment(Environment environment) {
		variableMap = new HashMap<>();
		functionMap = new HashMap<>();
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

	public String[] getFunctionsName() {
		return this.functionMap.keySet().toArray(new String[0]);
	}

	public String[] getParentFunctionsName() {
		if (this.parent == this || this.parent == null) {
			return new String[0];
		}
		return this.parent.getAllFunctionsName();
	}

	public String[] getAllFunctionsName() {
		String[] current = getFunctionsName();
		String[] parent = getParentFunctionsName();
		String[] all = new String[current.length + parent.length];
		System.arraycopy(current, 0, all, 0, current.length);
		System.arraycopy(parent, 0, all, current.length, parent.length);
		return all;
	}
	
	public String[] getVariablesName() {
		return this.variableMap.keySet().toArray(new String[0]);
	}

	public String[] getParentVariablesName() {
		if (this.parent == this || this.parent == null) {
			return new String[0];
		}
		return this.parent.getAllVariablesName();
	}

	public String[] getAllVariablesName() {
		String[] current = getVariablesName();
		String[] parent = getParentVariablesName();
		String[] all = new String[current.length + parent.length];
		System.arraycopy(current, 0, all, 0, current.length);
		System.arraycopy(parent, 0, all, current.length, parent.length);
		return all;
	}
}
