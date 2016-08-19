package net.m0cchi.function.java;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.m0cchi.function.java.util.JavaUtil;
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
			Constructor<?> constructor = JavaUtil.findConstructor(clazz, argsType.toArray(new Class[0]));

			Object value = constructor.newInstance(argsList.toArray(new Object[0]));
			ret = new Value<Object>(AtomicType.JAVA, value);
		} catch (Throwable e) {
			ret = new SList();
			e.printStackTrace();
		}
		return ret;
	}

}
