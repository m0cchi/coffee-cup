package net.m0cchi.value.i;

import net.m0cchi.value.Element;

public interface ListAPI {
	Element car();

	Element cdr();

	Element get(int i);

	int length();

	boolean isEmpty();

	Element[] toArray();

	void add(int index, Element element);

	void add(Element element);

}
