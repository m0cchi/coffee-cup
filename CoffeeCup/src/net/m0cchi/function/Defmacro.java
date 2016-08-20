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

public class Defmacro extends Defun {
	private static final long serialVersionUID = 8612914821309339930L;

	public Defmacro() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected Function buildFunction(final String name, final SList args, final Element[] body) {
		return new Macro() {
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

}
