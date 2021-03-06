package net.m0cchi.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class SyntaxAnalyzer {
	protected AbstractLexicalAnalyzer lexicalAnalyzer;

	public SyntaxAnalyzer(AbstractLexicalAnalyzer lexicalAnalyzer) {
		this.lexicalAnalyzer = lexicalAnalyzer;
	}

	protected Element parse(Element first) {
		final AtomicType type = first.getType();
		if (type == AtomicType.LEFT_PARENTHESIS) {
			lexicalAnalyzer.push(first);
			ListSyntaxAnalyzer syntaxAnalyzer = new ListSyntaxAnalyzer(lexicalAnalyzer);
			return syntaxAnalyzer.parse();
		} else if (type == AtomicType.QUOTE || type == AtomicType.QUASI_QUOTE) {
			Element second = parse(lexicalAnalyzer.take());
			if (second.getType() == AtomicType.SYMBOL || second.getType() == AtomicType.SLIST) {
				return new Value<Element>(type, second);
			}
		}
		return first;
	}

	public SList parse() {
		Element value;
		List<Element> list = new ArrayList<>();
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			list.add(parse(value));
		}
		return new SList(list);
	}

}
