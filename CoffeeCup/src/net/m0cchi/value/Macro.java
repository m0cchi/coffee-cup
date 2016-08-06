package net.m0cchi.value;


public abstract class Macro extends Function {

	public Macro() {
		super();
		this.type = AtomicType.MACRO;
	}

	@Override
	public void defineVariable(Environment environment, String name, Element value) {
		environment.defineVariable(name, value);
	}

}
