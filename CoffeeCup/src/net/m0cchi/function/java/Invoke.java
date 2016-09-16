package net.m0cchi.function.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.m0cchi.function.java.util.JavaUtil;
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
		Value<String> name = environment.getValue(getArgs()[0]);
		Value<?> instance = environment.getValue(getArgs()[1]);
		instance = semanticAnalyzer.evaluate(instance);

		SList args = environment.getValue(getArgs()[3]);
		Element ret = null;
		List<Class<?>> argsType = new ArrayList<>();
		List<Object> argsList = new ArrayList<>();
		for (Element element : args.getNativeValue()) {
			Value<?> value = semanticAnalyzer.evaluate(element);
			Object object = value.getNativeValue();
			argsType.add(object.getClass());
			argsList.add((object instanceof NULL) ? null : object);
		}

		try {
			Class<?> clazz = instance.getNativeValue().getClass();
			
			Method method = JavaUtil.findMethod(clazz, name.getNativeValue(), argsType.toArray(new Class[0]));

			Object[] argsArray = argsList.size() == 0 ? null : argsList.toArray(new Object[0]);
			Object object = method.invoke(instance.getNativeValue(), argsArray);
			if (object instanceof Element) {
				ret = (Element) object;
			} else if (object != null) {
				ret = new Value<Object>(AtomicType.JAVA, object);
			} else {
				ret = NIL.NIL;
			}

		} catch (Throwable e) {
			// TODO: other exception
			ret = new SList();
			e.printStackTrace();
		}

		return ret;
	}
}
