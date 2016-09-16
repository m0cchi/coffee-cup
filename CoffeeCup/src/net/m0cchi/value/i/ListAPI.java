package net.m0cchi.value.i;

import net.m0cchi.value.Element;

public interface ListAPI {
	<T extends Element> T car();

	Element cdr();

	<T extends Element> T get(int i);

	int length();

	boolean isEmpty();

	Element[] toArray();

	ListAPI add(int index, Element element);

	void addE(int index, Element element);

	ListAPI add(Element element);

	void addE(Element element);
}
