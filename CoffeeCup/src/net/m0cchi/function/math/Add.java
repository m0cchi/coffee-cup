package net.m0cchi.function.math;

import java.util.Arrays;
import java.util.Iterator;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Add extends Function {

	public Add() {
		setArgs(new String[] { REST, "add number" });
	}

	@Override
	public Element invoke(Environment environment) {
		SList args = (SList) environment.getValue(getArgs()[1]);
		Integer tmp = 0;
		Iterator<Element> iterator = Arrays.asList(args.toArray()).iterator();
		while (iterator.hasNext()) {
			Object object = ((Value<?>) iterator.next()).getNativeValue();
			if (object instanceof Integer) {
				tmp += (Integer) object;
			} else if(object instanceof Double) {
				tmp += ((Double) object).intValue();
			} else {
				// TODO: Error
			}
		}
		
		return new Value<Integer>(AtomicType.DIGIT, tmp);
	}

}
