package net.m0cchi.value;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.value.i.ListAPI;

public class SList extends Value<List<Element>> implements ListAPI {
	private static final long serialVersionUID = -1075335927989158821L;

	public SList(List<Element> list) {
		super(AtomicType.SLIST, list);
	}

	public SList() {
		this(new ArrayList<>());
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}

	@Override
	public Element[] toArray() {
		return value.toArray(new Element[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Element> T get(int index) {
		return (T) value.get(index);
	}

	@Override
	public void add(int index, Element element) {
		this.value.add(index, element);
	}

	@Override
	public void add(Element element) {
		this.value.add(element);
	}
	
	@Override
	public SList cdr() {
		List<Element> list = new ArrayList<>(this.value);
		list.remove(0);
		return new SList(list);
	}

	@Override
	public <T extends Element> T car() {
		return get(0);
	}

	@Override
	public int length() {
		return value.size();
	}

}
