package net.m0cchi.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class ListSyntaxAnalyzer extends SyntaxAnalyzer {

	public ListSyntaxAnalyzer(AbstractLexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
	}

	public static boolean isFirst(Value value) {
		return value.getType() == AtomicType.LEFT_PARENTHESIS;
	}
	
	public Value parse() {
		AtomicValue value = lexicalAnalyzer.take();
		List<Value> list = new ArrayList<>();
		
		if(!isFirst(value)) {
			// TODO: throw exception
			return new SList();
		} else {
			list.add(value);
		}
		
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			if (value.getType() == AtomicType.RIGHT_PARENTHESIS) {
				list.add(value);
				break;
			}
			list.add(parse(value));
		}

		return new SList(list);
	}

}
