package net.m0cchi.function;

import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;

public class NewList extends Function {
	private static final long serialVersionUID = 979011968472839107L;

	public NewList() {
		setArgs(REST, "new list");
	}

	@Override
	public Element invoke(Environment environment) {
		SList list = environment.getValue(getArgs()[1]);
		return list;
	}

}
