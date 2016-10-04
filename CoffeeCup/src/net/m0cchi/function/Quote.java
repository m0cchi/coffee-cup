package net.m0cchi.function;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.Value;

public class Quote extends Macro {
	private static final long serialVersionUID = 8850852154384679225L;

	public Quote() {
		setArgs("quote value");
	}

	@Override
	public Element invoke(Environment environment) {
		Element element = environment.getValue(getArgs()[0]);
		return new Value<Element>(AtomicType.QUOTE, element);
	}

}
