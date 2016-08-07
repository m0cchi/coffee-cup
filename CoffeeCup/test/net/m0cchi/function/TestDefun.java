package net.m0cchi.function;

import static org.junit.Assert.assertSame;
import net.m0cchi.function.math.Add;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.Test;

public class TestDefun {

	@SuppressWarnings("unchecked")
	@Test
	public void testDefun() {
		Environment environment = new Environment();
		environment.defineFunction("defun", new Defun());
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defun new-100 () 100)" + "(new-100)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);

		Element[] values = ((SList) syntaxAnalyzer.parse()).toArray();
		Element result = null;
		for (Element value : values) {
			result = semanticAnalyzer.evaluate(value);
		}
		assertSame(AtomicType.DIGIT, result.getType());
		assertSame(((Value<Integer>) result).getNativeValue(), 100);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDefun2() {
		Environment environment = new Environment();
		environment.defineFunction("defun", new Defun());
		environment.defineFunction("+", new Add());
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defun new-100 (one two) (+ one two))" + "(new-100 4 5)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);

		Element[] values = ((SList) syntaxAnalyzer.parse()).toArray();
		Element result = null;
		for (Element value : values) {
			result = semanticAnalyzer.evaluate(value);
		}
		assertSame(AtomicType.DIGIT, result.getType());
		assertSame(((Value<Integer>) result).getNativeValue(), 9);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDefun3() {
		Environment environment = new Environment();
		environment.defineFunction("defun", new Defun());
		environment.defineFunction("+", new Add());
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defun new-100 (&rest args) args)" + "(new-100 4 5)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);

		Element[] values = ((SList) syntaxAnalyzer.parse()).toArray();
		Element result = null;
		for (Element value : values) {
			result = semanticAnalyzer.evaluate(value);
		}
		assertSame(AtomicType.SLIST, result.getType());
		assertSame(((Value<Integer>)((SList) result).get(0)).getNativeValue(), 4);
		assertSame(((Value<Integer>)((SList) result).get(1)).getNativeValue(), 5);
		assertSame(((SList) result).toArray().length, 2);
	}

}
