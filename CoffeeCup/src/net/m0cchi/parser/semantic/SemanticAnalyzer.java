package net.m0cchi.parser.semantic;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class SemanticAnalyzer implements ISemanticAnalyzer {
	private Environment environment;

	public SemanticAnalyzer(Environment environment) {
		this.environment = environment;
	}

	public SemanticAnalyzer() {
		this(new Environment());
	}

	private Function internFunction(Element element) {
		if (element instanceof Value) {
			Object object = ((Value<?>) element).getNativeValue();
			if (object instanceof Function) {
				return (Function) object;
			} else {
				return environment.getFunction(object.toString());
			}
		}
		return null;
	}

	public Element evaluate(SList value) {
		Element head = value.get(0);
		Function function = internFunction(head);
		Element[] args = value.cdr().toArray();
		Element ret = function.invoke(this.environment, args);
		if (function instanceof Macro) {
			ret = evaluate(ret);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Element evaluate(Element value) {
		Element ret = null;
		AtomicType type = value.getType();
		switch (type) {
		case SLIST:
			SList slist = (SList) value;
			if (slist.isEmpty()) {
				ret = value;
			} else {
				ret = evaluate(slist);
			}
			break;
		case SYMBOL:
			String name = ((Value<String>) value).getNativeValue();
			ret = environment.getValue(name);
			break;
		case QUOTE:
			ret = ((Value<Element>) value).getNativeValue();
			break;
		default:
			ret = value;
		}
		return ret;
	}
}
