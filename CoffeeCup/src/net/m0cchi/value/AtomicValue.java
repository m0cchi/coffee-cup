package net.m0cchi.value;

public class AtomicValue extends Value {
	private String nativeValue;
	
	public AtomicValue(AtomicType type,String nativeValue) {
		this.type = type;
		this.nativeValue = nativeValue;
	}

	public String getNativeValue() {
		return nativeValue;
	}

	public void setNativeValue(String nativeValue) {
		this.nativeValue = nativeValue;
	}
	
}
