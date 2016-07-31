package net.m0cchi.parser.semantic;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class SemanticAnalyzer {
	public SemanticAnalyzer() {
	}

	public Value evaluate(SList value) {
		return null;
	}

	public Value evaluate(Value value) {
		Value ret = null;
		AtomicType type = value.getType();
		switch (type) {
		case SLIST:
			ret = evaluate((SList) value);
			break;
		default:
			ret = value;
		}
		return ret;
	}
}
