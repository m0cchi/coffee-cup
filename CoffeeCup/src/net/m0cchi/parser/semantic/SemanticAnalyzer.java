package net.m0cchi.parser.semantic;

import java.util.Arrays;
import java.util.List;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.exception.handler.Info;
import net.m0cchi.exception.handler.Signal;
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
			return environment.getFunction(object.toString());
		} else if (element instanceof Function) {
			return (Function) element;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T extends Element> T invokeFunction(Element head, Element[] args) {
		Function function = internFunction(head);
		if (function == null) {
			Signal info = new Info("func:" + head + ",args:" + Arrays.toString(args));
			Abort abort = new Abort();
			abort.addSuppressed(info);
			abort.addSuppressed(new NullPointerException(head.toString()));
			throw abort;
		}
		Element ret = null;
		try {
			ret = function.invoke(this.environment, args);
			if (function instanceof Macro) {
				ret = evaluate(ret);
			}
		} catch (Throwable signal) {
			Signal info = new Info("func:" + head + ",args:" + Arrays.toString(args));
			signal.addSuppressed(info);
			throw signal;
		}
		return (T) ret;
	}

	public <T extends Element> T evaluate(List<Element> value) {
		Element head = value.get(0);
		Element[] args = value.subList(1, value.size()).toArray(new Element[0]);
		return invokeFunction(head, args);
	}

	public <T extends Element> T evaluate(SList value) {
		Element head = value.get(0);
		Element[] args = value.cdr().toArray();
		return invokeFunction(head, args);
	}

	@SuppressWarnings("unchecked")
	public <T extends Element> T evaluate(Element value) {
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
		return (T) ret;
	}

}
