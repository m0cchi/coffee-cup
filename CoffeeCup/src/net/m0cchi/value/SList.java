package net.m0cchi.value;

import java.util.ArrayList;
import java.util.List;

public class SList extends Value<List<Element>> {
	private static final long serialVersionUID = -1075335927989158821L;

	public SList(List<Element> list) {
		super(AtomicType.SLIST, list);
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
