package net.m0cchi.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.SList;

public class ListSyntaxAnalyzer extends SyntaxAnalyzer {

	public ListSyntaxAnalyzer(AbstractLexicalAnalyzer lexicalAnalyzer) {
		super(lexicalAnalyzer);
	}

	public static boolean isFirst(Element value) {
		return value.getType() == AtomicType.LEFT_PARENTHESIS;
	}
	
	public Element parse() {
		Element value = lexicalAnalyzer.take();
		List<Element> list = new ArrayList<>();
		
		if(!isFirst(value)) {
			// TODO: throw exception
			return new SList();
		}
		
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			if (value.getType() == AtomicType.RIGHT_PARENTHESIS) {
				break;
			}
			list.add(parse(value));
		}

		return new SList(list);
	}

}
