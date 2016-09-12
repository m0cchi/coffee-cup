package net.m0cchi.function.java.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.m0cchi.value.NULL;

@SuppressWarnings("serial")
public class JavaUtil {
	private static final Map<Class<?>, Class<?>> PRIMITIVE_CLASS_MAP;

	static {
		PRIMITIVE_CLASS_MAP = new HashMap<Class<?>, Class<?>>() {
			{
				put(int.class, Integer.class);
				put(byte.class, Byte.class);
				put(long.class, Long.class);
				put(short.class, Short.class);
				put(float.class, Float.class);
				put(double.class, Double.class);
				put(boolean.class, Boolean.class);
				put(char.class, Character.class);
			}
		};
	}

	public static Class<?> boxing(Class<?> clazz) {
		return PRIMITIVE_CLASS_MAP.get(clazz);
	}

	private static AccessibleObject toAccessible(AccessibleObject method) {
		if (method != null && !method.isAccessible()) {
			method.setAccessible(true);
		}
		return method;
	}

	public static Method findMethod(Class<?> clazz, String name, Class<?>[] argsType) {
		if (clazz == null)
			return null;
		Method[] methods = clazz.getDeclaredMethods();
		Method ret = null;

		List<Method> candidates = new ArrayList<>();
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			matching: {
				if (method.getName().equals(name) && parameterTypes.length == argsType.length) {
					boolean isAssignable = false;
					for (int i = 0; i < parameterTypes.length; i++) {
						if (argsType[i] == NULL.class || parameterTypes[i].equals(argsType[i])) {
							continue;
						}

						isAssignable |= parameterTypes[i].isAssignableFrom(argsType[i]);
						if (!isAssignable) {
							// check primitive
							if (!(parameterTypes[i].isPrimitive() && boxing(parameterTypes[i]).equals(argsType[i]))) {
								break matching;
							}
						}
					}
					if (!isAssignable) {
						ret = method;
						break;
					} else {
						candidates.add(method);
					}
				}
			}
		}

		if (ret == null) {
			if (!candidates.isEmpty()) {
				ret = candidates.get(0);
			} else {
				ret = findMethod(clazz.getSuperclass(), name, argsType);
			}
		}

		return (Method) toAccessible(ret);
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
							// check primitive
							if (!(parameterTypes[i].isPrimitive() && boxing(parameterTypes[i]).equals(argsType[i]))) {
								break matching;
							}
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
