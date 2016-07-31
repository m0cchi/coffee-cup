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
	
	public Value get(int index) {
		return list.get(index);
	}
	
	public SList cdr() {
		List<Value> list = new ArrayList<>(this.list);
		list.remove(0);
		return new SList(list);
	}
}
