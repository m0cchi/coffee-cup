package net.m0cchi.function.java;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class AppendLoadPath extends Function {

	public AppendLoadPath() {
		setArgs(new String[] { "java path" });
	}

	@Override
	public Element invoke(Environment environment) {
		@SuppressWarnings("unchecked")
		Value<String> pathData = (Value<String>) environment.getValue(getArgs()[0]);

		File path = new File(pathData.getNativeValue());

		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(environment.getClassLoader(), path.toURI().toURL());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new SList();
	}

}
