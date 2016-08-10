package net.m0cchi.function.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.NULL;
import net.m0cchi.value.NULL.NIL;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Invoke extends Macro {
	private static final long serialVersionUID = -2285797109827350985L;

	public Invoke() {
		setArgs(new String[] { "java method name", "java instanse", REST, "java args" });
	}

	@Override
	public Element invoke(Environment environment) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		@SuppressWarnings("unchecked")
		Value<String> name = (Value<String>) environment.getValue(getArgs()[0]);
		Value<?> instance = (Value<?>) environment.getValue(getArgs()[1]);
		instance = (Value<?>) semanticAnalyzer.evaluate(instance);

		SList args = (SList) environment.getValue(getArgs()[3]);
		Element ret = null;
		List<Class<?>> argsType = new ArrayList<>();
		List<Object> argsList = new ArrayList<>();
		for (Element element : args.getNativeValue()) {
			Value<?> value = (Value<?>) semanticAnalyzer.evaluate(element);
			Object object = value.getNativeValue();
			argsType.add(object.getClass());
			argsList.add((object instanceof NULL) ? null : object);
		}

		try {
			Class<?> clazz = instance.getNativeValue().getClass();
			Method method = findMethod(clazz, name.getNativeValue(), argsType.toArray(new Class[0]));

			Object object = method.invoke(instance.getNativeValue(), argsList.toArray(new Object[0]));
			if (object instanceof Element) {
				ret = (Element) object;
			} else if (object != null) {
				ret = new Value<Object>(AtomicType.JAVA, object);
			} else {
				ret = new NIL();
			}

		} catch (Throwable e) {
			// TODO: other exception
			ret = new SList();
			e.printStackTrace();
		}

		return ret;
	}

	public static Method findMethod(Class<?> clazz, String name, Class<?>[] argsType) {
		if (clazz == null)
			return null;
		Method[] methods = clazz.getDeclaredMethods();

		List<Method> candidates = new ArrayList<>();
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			matching: {
				if (method.getName().equals(name) && parameterTypes.length == argsType.length) {
					boolean isAssignable = false;
					for (int i = 0; i < parameterTypes.length; i++) {
						if (argsType[i] == NULL.class) {
							continue;
						}
						if (!(parameterTypes[i].equals(argsType[i]) || !(isAssignable |= argsType[i].isAssignableFrom(parameterTypes[i])))) {
							break matching;
						}
					}
					if (!isAssignable) {
						return method;
					} else {
						candidates.add(method);
					}
				}
			}
		}
		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return findMethod(clazz.getSuperclass(), name, argsType);
	}
}
