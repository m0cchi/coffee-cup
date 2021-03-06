package net.m0cchi.function.data;

public class Super {

	public String field = "super";
	public static String staticField = "super-static";

	public String hoge(Super obj) {
		return obj.field;
	}

	public String override() {
		return this.field;
	}

	public String overload(Super obj) {
		return obj.field;
	}

	public String notNull(Super obj) {
		return obj != null ? "not null" : "null";
	}

	public int boxing(int i) {
		return i * 2;
	}
}
