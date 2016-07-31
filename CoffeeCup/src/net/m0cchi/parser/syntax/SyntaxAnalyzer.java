package net.m0cchi.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class SyntaxAnalyzer {
	protected AbstractLexicalAnalyzer lexicalAnalyzer;

	public SyntaxAnalyzer(AbstractLexicalAnalyzer lexicalAnalyzer) {
		this.lexicalAnalyzer = lexicalAnalyzer;
	}
	
	protected Value parse(AtomicValue first) {
		AtomicType type = first.getType();
		if (type == AtomicType.LEFT_PARENTHESIS) {
			lexicalAnalyzer.push(first);
			ListSyntaxAnalyzer syntaxAnalyzer = new ListSyntaxAnalyzer(lexicalAnalyzer);
			return syntaxAnalyzer.parse();
		}
		return first;
	}

	public Value parse() {
		AtomicValue value;
		List<Value> list = new ArrayList<>();
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			list.add(parse(value));
		}
		return new SList(list);
	}

}
