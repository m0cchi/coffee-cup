package net.m0cchi.value;

public class AtomicValue {
	private AtomicType type;
	private String nativeValue;
	
	public AtomicValue(AtomicType type,String nativeValue) {
		this.type = type;
		this.nativeValue = nativeValue;
	}
}
