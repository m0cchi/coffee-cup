package net.m0cchi.function.remote;

import net.m0cchi.parser.semantic.ISemanticAnalyzer;
import net.m0cchi.remote.semantic.RemoteSemanticAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Value;

public class Connect extends Function {
	private static final long serialVersionUID = -7964601672743072879L;

	public Connect() {
		setArgs("host name", "host port");
	}

	@Override
	public Element invoke(Environment environment) {
		@SuppressWarnings("unchecked")
		Value<String> hostValue = (Value<String>) environment.getValue(getArgs()[0]);
		@SuppressWarnings("unchecked")
		Value<Integer> portValue = (Value<Integer>) environment.getValue(getArgs()[1]);
		String host = hostValue.getNativeValue();
		Integer port = portValue.getNativeValue();
		ISemanticAnalyzer semanticAnalyzer = new RemoteSemanticAnalyzer(host, port);
		return new Value<ISemanticAnalyzer>(AtomicType.JAVA, semanticAnalyzer);
	}

}
