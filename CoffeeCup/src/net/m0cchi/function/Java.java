package net.m0cchi.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Java {

	public static Method findMethod(Class<?> clazz, String name, Class<?>[] argsType) {
		if(clazz == null) return null;
		Method[] methods = clazz.getDeclaredMethods();
		
		List<Method> candidates = new ArrayList<>();
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			matching: {
				if (method.getName().equals(name) && parameterTypes.length == argsType.length) {
					boolean isAssignable = false;
					for (int i = 0; i < parameterTypes.length; i++) {
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

	public static class New extends Macro {
		public New() {
			setArgs(new String[] { "java clazz", "java args" });
		}

		@Override
		public Element invoke(Environment environment) {
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
			@SuppressWarnings("unchecked")
			Value<String> name = (Value<String>) environment.getValue(getArgs()[0]);
			SList args = (SList) environment.getValue(getArgs()[1]);
			Element ret = null;

			List<Class<?>> argsType = new ArrayList<>();
			List<Object> argsList = new ArrayList<>();
			for (Element element : args.getNativeValue()) {
				Value<?> value = (Value<?>) semanticAnalyzer.evaluate(element);
				argsType.add(value.getNativeValue().getClass());
				argsList.add(value.getNativeValue());
			}

			try {
				Class<?> clazz = Class.forName(name.getNativeValue(), true, environment.getClassLoader());
				Constructor<?> constructor = clazz.getConstructor(argsType.toArray(new Class[0]));

				Object value = constructor.newInstance(argsList.toArray(new Object[0]));
				ret = new Value<Object>(AtomicType.JAVA, value);
			} catch (Throwable e) {
				ret = new SList();
				e.printStackTrace();
			}
			return ret;
		}
	}

	public static class Invoke extends Macro {

		public Invoke() {
			setArgs(new String[] { "java method name", "java instanse", "java args" });
		}

		@Override
		public Element invoke(Environment environment) {
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
			@SuppressWarnings("unchecked")
			Value<String> name = (Value<String>) environment.getValue(getArgs()[0]);
			Value<?> instance = (Value<?>) environment.getValue(getArgs()[1]);
			instance = (Value<?>) semanticAnalyzer.evaluate(instance);

			SList args = (SList) environment.getValue(getArgs()[2]);
			Element ret = null;
			List<Class<?>> argsType = new ArrayList<>();
			List<Object> argsList = new ArrayList<>();
			for (Element element : args.getNativeValue()) {
				Value<?> value = (Value<?>) semanticAnalyzer.evaluate(element);
				argsType.add(value.getNativeValue().getClass());
				argsList.add(value.getNativeValue());
			}

			try {
				Class<?> clazz = instance.getNativeValue().getClass();
				Method method = Java.findMethod(clazz, name.getNativeValue(), argsType.toArray(new Class[0]));
				// clazz.getMethod(name.getNativeValue(), argsType.toArray(new
				// Class[0]));
				Object object = method.invoke(instance.getNativeValue(), argsList.toArray(new Object[0]));
				if (object instanceof Element) {
					ret = (Element) object;
				} else if(object != null){
					ret = new Value<Object>(AtomicType.JAVA, object);
				} else {
					ret = new SList();
				}

			} catch (Throwable e) {
				// TODO: other exception
				ret = new SList();
				e.printStackTrace();
			}

			return ret;
		}

	}
}
