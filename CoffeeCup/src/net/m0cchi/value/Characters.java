package net.m0cchi.value;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.value.i.ListAPI;

public class Characters extends Value<String> implements ListAPI {
	private static final long serialVersionUID = 5812304774246773545L;

	public Characters(String value) {
		super(AtomicType.LETTER, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Characters car() {
		return get(0);
	}

	@Override
	public Element cdr() {
		int len = value.length();
		return new Characters(value.substring(1, len - 1));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Characters get(int i) {
		return new Characters(value.substring(i, i + 1));
	}

	@Override
	public int length() {
		return value.length();
	}

	@Override
	public boolean isEmpty() {
		return value == null || value.length() == 0;
	}

	@Override
	public Element[] toArray() {
		Element[] elements = new Element[value.length()];
		for (int i = 0, len = value.length(); i < len; i++) {
			elements[i] = get(i);
		}
		return elements;
	}

	@Override
	public void addE(int index, Element element) {
		if (element instanceof Value) {
			StringBuilder sb = new StringBuilder();
			sb.append(value.substring(0, index));
			sb.append(((Value<?>) element).getNativeValue().toString());
			sb.append(value.subSequence(index, value.length() - 1));
			value = sb.toString();
		} else {
			throw new Abort();
		}
	}

	@Override
	public void addE(Element element) {
		if (element instanceof Value) {
			String string = ((Value<?>) element).getNativeValue().toString();
			StringBuilder sb = new StringBuilder();
			sb.append(value);
			sb.append(string);
			value = sb.toString();
		} else {
			throw new Abort();
		}
	}

	public Element substring(Element start, Element end) {
		@SuppressWarnings("unchecked")
		int startNum = ((Value<Integer>) start).getNativeValue();
		@SuppressWarnings("unchecked")
		int endNum = ((Value<Integer>) end).getNativeValue();
		String str = value.substring(startNum, endNum);
		return new Characters(str);
	}

	public Element substring(Element start) {
		@SuppressWarnings("unchecked")
		int startNum = ((Value<Integer>) start).getNativeValue();
		String str = value.substring(startNum);
		return new Characters(str);
	}

	public Element indexOf(Element element) {
		int index = -1;
		if (element instanceof Value) {
			String target = ((Value<?>) element).getNativeValue().toString();
			index = value.indexOf(target);
		}
		return new Value<Integer>(AtomicType.DIGIT, index);
	}

	@Override
	public ListAPI add(int index, Element element) {
		String copy = new String(this.value);
		Characters characters = new Characters(copy);
		characters.add(index, element);
		return characters;
	}

	@Override
	public ListAPI add(Element element) {
		String copy = new String(this.value);
		Characters characters = new Characters(copy);
		characters.add(element);
		return characters;
	}

}
