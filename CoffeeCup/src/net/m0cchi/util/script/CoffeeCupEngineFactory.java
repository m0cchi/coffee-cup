package net.m0cchi.util.script;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import net.m0cchi.value.Environment;

public class CoffeeCupEngineFactory implements ScriptEngineFactory {
	
	private Environment env;
	public CoffeeCupEngineFactory() {
		env = new Environment();
	}
	public CoffeeCupEngineFactory(Environment environment) {
		this.env = environment;
	}

	@Override
	public String getEngineName() {
		return "coffee-cup";
	}

	@Override
	public String getEngineVersion() {
		return "0.0.1";
	}

	@SuppressWarnings("serial")
	@Override
	public List<String> getExtensions() {
		return new ArrayList<String>() {
			{
				add("lisp");
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	public List<String> getMimeTypes() {
		return new ArrayList<String>() {
			{
				add("application/x-coffee-cup");
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	public List<String> getNames() {
		return new ArrayList<String>() {
			{
				add(getEngineName());
				add("lisp");
			}
		};
	}

	@Override
	public String getLanguageName() {
		return getEngineName();
	}

	@Override
	public String getLanguageVersion() {
		return "0.0.1";
	}

	@Override
	public Object getParameter(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProgram(String... statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new CoffeeCupEngine(env);
	}

}
