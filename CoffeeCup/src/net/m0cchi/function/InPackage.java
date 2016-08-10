package net.m0cchi.function;

import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class InPackage extends Function {
	private static final long serialVersionUID = -6716392359470729387L;

	public InPackage() {
		setArgs(new String[] { "in-package name" });
	}

	@Override
	public Element invoke(Environment environment) {
		@SuppressWarnings("unchecked")
		Value<String> name = (Value<String>) environment.getValue(getArgs()[0]);
		if (Environment.hasPackage(name.getNativeValue())) {
			environment.getParent().load(name.getNativeValue());
		} else {
			environment.getParent().naming(name.getNativeValue());
		}
		return new SList();
	}

}
