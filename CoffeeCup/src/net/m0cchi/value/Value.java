package net.m0cchi.value;

import java.io.Serializable;

public class Value<T> extends Element implements Serializable {
	private static final long serialVersionUID = 5935896451124681354L;
	protected T value;

	public Value(AtomicType type, T value) {
		this.type = type;
		this.value = value;
	}

	public T getNativeValue() {
		return value;
	}
}
