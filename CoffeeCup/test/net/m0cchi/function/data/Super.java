package net.m0cchi.function.data;


public class Super {

	protected String field = "super";

	public String hoge(Super obj) {
		return obj.field;
	}


	public String override() {
		return this.field;
	}
	
	public String overload(Super obj) {
		return obj.field;
	}
}
