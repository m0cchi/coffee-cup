package net.m0cchi.value;

import java.util.ArrayList;
import java.util.List;

public class SList extends Value {
	private List<Value> list;

	public SList(List<Value> list) {
		this.list = list;
		this.type = AtomicType.SLIST;
	}

	public SList() {
		this(new ArrayList<>());
	}

	public Value[] toArray() {
		return list.toArray(new Value[0]);
	}
}
