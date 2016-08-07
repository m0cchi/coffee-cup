package net.m0cchi.function.data;

public class Sub extends Super {
	{
		this.field = "sub";
	}

	public String override() {
		return this.field + ":override";
	}
	
	public String overload(Sub obj) {
		return obj.field + "overload";
	}
}
