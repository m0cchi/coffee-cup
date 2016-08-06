package net.m0cchi.value;

public class Value<T> extends Element {
	protected T value;
	
	public Value(AtomicType type,T value) {
		this.type = type;
		this.value = value;
	}
	
	public T getNativeValue() {
		return value;
	}
}
