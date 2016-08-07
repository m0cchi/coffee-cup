package net.m0cchi.value;

import java.util.List;

public abstract class Macro extends Function {

	{
		init();
		this.type = AtomicType.MACRO;
	}

	@Override
	protected void defineVariable(Environment environment, String name, Element value) {
		environment.defineVariable(name, value);
	}

	@Override
	protected void defineVariable(Environment environment, String name, List<Element> list) {
		environment.defineVariable(name, new SList(list));
	}

}
