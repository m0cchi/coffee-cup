package net.m0cchi.function.handler;

import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;

public class DoList extends Function {
	private static final long serialVersionUID = -6888355295564890338L;

	public DoList() {
		setArgs(new String[] { REST, "do list" });
	}

	@Override
	public Element invoke(Environment environment) {
		SList list = (SList) environment.getValue(getArgs()[1]);
		Element[] values = list.toArray();
		return values[values.length - 1];
	}

}
