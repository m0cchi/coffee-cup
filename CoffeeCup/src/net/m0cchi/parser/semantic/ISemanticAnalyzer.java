package net.m0cchi.parser.semantic;

import net.m0cchi.value.Element;

public interface ISemanticAnalyzer {
	<T extends Element> T evaluate(Element value);
}
