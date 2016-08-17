package net.m0cchi.function.java;

import java.lang.reflect.Field;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.NULL.NIL;
import net.m0cchi.value.Value;
/**
 * can't access java.lang.Class
 */
public class GetField extends Macro {
	private static final long serialVersionUID = 921013854187555241L;

	public GetField() {
		setArgs("field name", "class instance");
	}

	@Override
	public Element invoke(Environment environment) {
		@SuppressWarnings("unchecked")
		Value<String> fieldNameValue = (Value<String>) environment.getValue(getArgs()[0]);
		Value<?> instanceValue = (Value<?>) environment.getValue(getArgs()[1]);
		instanceValue = (Value<?>) new SemanticAnalyzer(environment).evaluate(instanceValue);
		Object instance = instanceValue.getNativeValue();
		boolean isClass = instance instanceof Class;
		@SuppressWarnings("unchecked")
		Class<?> clazz = isClass ? (Class<? extends Object>) instance : instance.getClass();
		Element ret = new NIL();
		try {
			Field field = clazz.getField(fieldNameValue.getNativeValue());
			Object value = field.get(isClass ? null : instance);
			ret = new Value<Object>(AtomicType.JAVA, value);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return ret;
	}

}
