package net.m0cchi.value;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

public class TestEnvironment {

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

}
