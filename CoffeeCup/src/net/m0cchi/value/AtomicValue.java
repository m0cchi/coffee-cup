package net.m0cchi.value;

public class AtomicValue extends Value<String> {
	
	public AtomicValue(AtomicType type,String nativeValue) {
		this.type = type;
		this.value = nativeValue;
	}
	
}
