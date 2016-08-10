package net.m0cchi.value;

import java.io.Serializable;

public abstract class Element implements Serializable {
	private static final long serialVersionUID = 1074149770541419138L;
	protected AtomicType type;

	public AtomicType getType() {
		return type;
	}

}
