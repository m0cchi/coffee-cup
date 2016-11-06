package net.m0cchi.value;

import java.io.Serializable;

public abstract class Element implements Serializable {
	private static final long serialVersionUID = 1074149770541419138L;
	protected AtomicType type;

	public AtomicType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * unsupport
	 * @deprecated
	 * @return
	 */
	public Element shallowCopy() {
		return this;
	}

}
