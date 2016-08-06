package net.m0cchi.value;

import java.util.ArrayList;
import java.util.List;

public class SList extends Value<List<Element>> {

	public SList(List<Element> list) {
		this.value = list;
		this.type = AtomicType.SLIST;
	}

	public SList() {
		this(new ArrayList<>());
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public Element[] toArray() {
		return value.toArray(new Value[0]);
	}

	public Element get(int index) {
		return value.get(index);
	}

	public SList cdr() {
		List<Element> list = new ArrayList<>(this.value);
		list.remove(0);
		return new SList(list);
	}

}
