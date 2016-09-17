package net.m0cchi.util.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.m0cchi.function.Defvar;
import net.m0cchi.function.math.Add;
import net.m0cchi.value.Environment;

public class FactorySample {

	public static void main(String[] args) throws ScriptException {
		Environment environment = new Environment();
		environment.defineFunction(new Defvar());
		environment.defineFunction("+", new Add());
		CoffeeCupEngineFactory factory = new CoffeeCupEngineFactory(environment);
		ScriptEngineManager manager = new ScriptEngineManager();
		manager.registerEngineName(factory.getEngineName(), factory);
		ScriptEngine engine = manager.getEngineByName(factory.getEngineName());
		System.out.println(engine.eval("(+ 1 2)"));
		engine.eval("(defvar name \"m0cchi\")");
		System.out.println(engine.get("name"));

	}

}
