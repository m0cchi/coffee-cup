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

	public String toString() {
		return value == null ? "null" : value.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value<?> other = (Value<?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public Value<T> shallowCopy() {
		return new Value<T>(type, value);
	}
}
