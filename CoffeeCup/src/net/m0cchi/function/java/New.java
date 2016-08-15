package net.m0cchi.function.java;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.NULL;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class New extends Macro {
	private static final long serialVersionUID = 6821382750912294248L;

	public New() {
		setArgs(new String[] { "java clazz", REST, "java args" });
	}

	@Override
	public Element invoke(Environment environment) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		@SuppressWarnings("unchecked")
		Value<String> name = (Value<String>) environment.getValue(getArgs()[0]);
		SList args = (SList) environment.getValue(getArgs()[2]);
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
			Class<?> clazz = environment.getClassLoader().loadClass(name.getNativeValue());
			Constructor<?> constructor = findConstructor(clazz, argsType.toArray(new Class[0]));

			Object value = constructor.newInstance(argsList.toArray(new Object[0]));
			ret = new Value<Object>(AtomicType.JAVA, value);
		} catch (Throwable e) {
			ret = new SList();
			e.printStackTrace();
		}
		return ret;
	}

	public static Constructor<?> findConstructor(Class<?> clazz, Class<?>[] argsType) {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();

		List<Constructor<?>> candidates = new ArrayList<>();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			matching: {
				if (parameterTypes.length == argsType.length) {
					boolean isAssignable = false;
					for (int i = 0; i < parameterTypes.length; i++) {
						if (argsType[i] == NULL.class || parameterTypes[i].equals(argsType[i])) {
							continue;
						}
						
						isAssignable |= parameterTypes[i].isAssignableFrom(argsType[i]);
						if (!isAssignable) {
							break matching;
						}
					}
					if (!isAssignable) {
						return constructor;
					} else {
						candidates.add(constructor);
					}
				}
			}
		}

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}
}
