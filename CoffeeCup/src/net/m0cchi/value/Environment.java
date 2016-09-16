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
		variableMap.put("nil", NIL.NIL);
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
		move(environment, this);
	}

	public void defineVariable(String name, Element element) {
		this.variableMap.put(name, element);
	}

	public void renameFunction(String oldName, String newName) {
		Function function = this.functionMap.remove(oldName);
		this.functionMap.put(newName, function);
	}
	
	public void renameVariable(String oldName, String newName) {
		Element obj = this.variableMap.remove(oldName);
		this.variableMap.put(newName, obj);
	}

	@SuppressWarnings("unchecked")
	public <T extends Element> T getValue(String name) {
		return (T) (this.variableMap.containsKey(name) ? this.variableMap.get(name) : this.parent != null ? this.parent.getValue(name) : new SList());
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
	
	public void defineFunction(Function function) {
		this.functionMap.put(function.getName(), function);
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

	public static void move(Environment origin, Environment to) {
		to.parent = origin.parent;
		to.functionMap = origin.functionMap;
		to.variableMap = origin.variableMap;
	}
}
