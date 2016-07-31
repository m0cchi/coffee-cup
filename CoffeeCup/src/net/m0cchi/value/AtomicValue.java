package net.m0cchi.value;

public class AtomicValue {
	private AtomicType type;
	private String nativeValue;
	
	public AtomicValue(AtomicType type,String nativeValue) {
		this.type = type;
		this.nativeValue = nativeValue;
	}

	public AtomicType getType() {
		return type;
	}

	public void setType(AtomicType type) {
		this.type = type;
	}

	public String getNativeValue() {
		return nativeValue;
	}

	public void setNativeValue(String nativeValue) {
		this.nativeValue = nativeValue;
	}
	
}
