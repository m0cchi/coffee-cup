package net.m0cchi.util.script;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import net.m0cchi.parser.lexical.StreamLexicalAnalyzer;
import net.m0cchi.util.Program;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Value;

public class CoffeeCupEngine implements ScriptEngine {

	private Program program;
	public CoffeeCupEngine(Environment environment) {
		this.program = new Program("");
		Environment origin = this.program.getEnvironment();
		Environment.move(environment, origin);
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		
		return null;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eval(String script) throws ScriptException {
		program.init(script);
		Element ret = program.execute();
		if(ret instanceof Value<?>) {
			return ((Value<?>)ret).getNativeValue();
		}
		return ret;
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(String script, Bindings n) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(String key) {
		Element ret = this.program.getEnvironment().getValue(key);
		if(ret instanceof Value<?>) {
			return ((Value<?>)ret).getNativeValue();
		}
		return ret;
	}

	@Override
	public Bindings getBindings(int scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public Bindings createBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(ScriptContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public ScriptEngineFactory getFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
