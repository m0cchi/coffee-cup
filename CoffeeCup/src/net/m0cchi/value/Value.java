package net.m0cchi.value;

public abstract class Value<T> extends Element {
	protected T value;
	
	public T getNativeValue() {
		return value;
	}
}
