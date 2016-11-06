package net.m0cchi.parser.semantic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.exception.handler.Info;
import net.m0cchi.exception.handler.Signal;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Macro;
import net.m0cchi.value.NULL.NIL;
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

	public SList unquote(List<? extends Element> list) {
		ArrayList<Element> ret = new ArrayList<>();
		Iterator<? extends Element> iterator = list.iterator();

		while (iterator.hasNext()) {
			Element element = iterator.next();
			if (AtomicType.COMMA.equals(element.getType())) {
				ret.add(evaluate(iterator.next()));
			} else if (element instanceof SList) {
				SList res = unquote(((SList) element).getNativeValue());
				ret.add(res);
			} else if (element instanceof Value<?>) {
				Object object = ((Value<?>) element);
				if (object instanceof List) {
					@SuppressWarnings("unchecked")
					SList res = unquote((List<? extends Element>) object);
					ret.add(res);
				} else {
					ret.add(element);
				}
			} else {
				ret.add(element);
			}
		}
		return new SList(ret);
	}

	@SuppressWarnings("unchecked")
	public Element unquote(Element element) {
		Element ret = NIL.NIL;
		if (element instanceof SList) {
			ret = unquote(((SList) element).getNativeValue());
		} else if (element instanceof Value<?>) {
			Value<?> slist = (Value<?>) element;
			Object object = slist.getNativeValue();
			if (object instanceof List) {
				ret = unquote((List<Element>) object);
			} else {
				ret = element;
			}
		} else {
			ret = element;
		}
		return ret;
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
		case QUASI_QUOTE:
			ret = unquote(((Value<Element>) value).getNativeValue());
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
