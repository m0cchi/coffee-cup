package net.m0cchi.function.remote;

import net.m0cchi.function.handler.DoList;
import net.m0cchi.parser.semantic.ISemanticAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Remote extends Macro {
	private static final long serialVersionUID = 2584846002850786875L;

	public Remote() {
		setArgs("remote connection", REST, "remote action");
	}

	@Override
	public Element invoke(Environment environment) {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		Element connection = environment.getValue(getArgs()[0]);

		@SuppressWarnings("unchecked")
		Value<ISemanticAnalyzer> semanticAnalyzerValue = (Value<ISemanticAnalyzer>) semanticAnalyzer.evaluate(connection);
		ISemanticAnalyzer remoteSemanticAnalyzer = semanticAnalyzerValue.getNativeValue();

		SList list = (SList) environment.getValue(getArgs()[2]);
		list.add(0, new DoList());

		return remoteSemanticAnalyzer.evaluate(list);
	}

}
