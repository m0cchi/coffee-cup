package net.m0cchi.function;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Defun extends Macro {
	private static final long serialVersionUID = 108758569589978951L;

	public Defun() {
		setArgs(new String[] { "defun name", "defun args", REST, "defun body" });
	}

	@SuppressWarnings("unchecked")
	protected Function buildFunction(final String name, final SList args, final Element[] body) {
		return new Function() {
			private static final long serialVersionUID = 1L;

			{
				List<String> argsList = new ArrayList<>();
				for (Element arg : args.toArray()) {
					argsList.add(((Value<String>) arg).getNativeValue());
				}
				setArgs(argsList.toArray(new String[0]));
			}

			@Override
			public Element invoke(Environment environment) {
				SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
				Element ret = null;
				for (Element element : body) {
					ret = semanticAnalyzer.evaluate(element);
				}
				return ret;
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

	@Override
	public Element invoke(Environment environment) {
		Value<String> name = environment.getValue(getArgs()[0]);
		final SList args = environment.getValue(getArgs()[1]);
		final Element[] body = ((SList) environment.getValue(getArgs()[3])).toArray();
		final String NAME = name.getNativeValue();

		Function function = buildFunction(NAME, args, body);

		environment.getParent().defineFunction(NAME, function);
		return new SList();
	}

}
