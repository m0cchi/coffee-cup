package net.m0cchi.parser.semantic;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class SemanticAnalyzer {
	private Environment environment;

	public SemanticAnalyzer(Environment environment) {
		this.environment = environment;
	}

	public SemanticAnalyzer() {
		this(new Environment());
	}

	public Element evaluate(SList value) {
		AtomicValue head = (AtomicValue) value.get(0);
		Function function = environment.getFunction(head.getNativeValue());
		Element ret = function.invoke(this.environment, value.cdr().toArray());
		if (function instanceof Macro) {
			ret = evaluate(ret);
		}
		return ret;
	}

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
			String name = ((AtomicValue) value).getNativeValue();
			ret = environment.getValue(name);
			break;
		case QUOTE:
			SList quote = (SList) value;
			ret = quote.get(0);
			break;
		default:
			ret = value;
		}
		return ret;
	}
}
