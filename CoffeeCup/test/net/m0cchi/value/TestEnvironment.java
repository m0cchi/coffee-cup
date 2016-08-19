package net.m0cchi.value;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

public class TestEnvironment {

	@SuppressWarnings("serial")
	@Test
	public void testSetValue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field variableMap = Environment.class.getDeclaredField("variableMap");
		variableMap.setAccessible(true);
		
		Environment top = new Environment();
		Environment middle = new Environment(top);
		Environment bottom = new Environment(middle);
		// set test data
		middle.defineVariable("target 1", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		bottom.defineVariable("target 2", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 3", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 2", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 1", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		// update
		for (String name : "1,2,3".split(",")) {
			bottom.setValue("target " + name, new Element() {
				{
					this.type = AtomicType.SYMBOL;
				}
			});
		}
		// assert
		@SuppressWarnings("unchecked")
		Map<String, Element> topMap = (Map<String, Element>) variableMap.get(top);
		assertSame(AtomicType.TERMINAL, topMap.get("target 1").getType());
		assertSame(AtomicType.TERMINAL, topMap.get("target 2").getType());
		assertSame(AtomicType.SYMBOL, topMap.get("target 3").getType());
		@SuppressWarnings("unchecked")
		Map<String, Element> middleMap = (Map<String, Element>) variableMap.get(middle);
		assertSame(AtomicType.SYMBOL, middleMap.get("target 1").getType());
		assertNull( middleMap.get("target 2"));
		assertNull(middleMap.get("target 3"));
		@SuppressWarnings("unchecked")
		Map<String, Element> bottomMap = (Map<String, Element>) variableMap.get(bottom);
		assertNull(bottomMap.get("target 1"));
		assertSame(AtomicType.SYMBOL, bottomMap.get("target 2").getType());
		assertNull(bottomMap.get("target 3"));
	}

	@SuppressWarnings("serial")
	@Test
	public void testGetFunctionsName(){
		Environment top = new Environment();
		Environment middle = new Environment(top);
		Environment bottom = new Environment(middle);
		Function function = new Function() {
			@Override
			public Element invoke(Environment environment) {
				return null;
			}
		};
		// set test data
		middle.defineFunction("target 1 middle", function);
		bottom.defineFunction("target 2 bottom", function);
		top.defineFunction("target 3 top", function);
		top.defineFunction("target 2 top", function);
		top.defineFunction("target 1 top", function);
		assertSame(3,top.getAllFunctionsName().length);
		assertSame(4,middle.getAllFunctionsName().length);
		assertSame(5,bottom.getAllFunctionsName().length);
		
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testGetVariablesName(){
		Environment top = new Environment();
		Environment middle = new Environment(top);
		Environment bottom = new Environment(middle);
		// set test data
		middle.defineVariable("target 1 middle", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		bottom.defineVariable("target 2 bottom", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 3 top", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 2 top", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		top.defineVariable("target 1 top", new Element() {
			{
				this.type = AtomicType.TERMINAL;
			}
		});
		// + 1 : nil
		assertSame(3 + 1,top.getAllVariablesName().length);
		assertSame(4 + 1,middle.getAllVariablesName().length);
		assertSame(5 + 1,bottom.getAllVariablesName().length);
	}

}
