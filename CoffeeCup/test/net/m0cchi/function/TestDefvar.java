package net.m0cchi.function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.Test;

public class TestDefvar {

	@Test
	public void test() {

		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defvar name 100)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Value ret = ((SList) syntaxAnalyzer.parse()).toArray()[0];
		Environment environment = new Environment();
		environment.defineFunction("defvar", new Defvar());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		Value nullValue = semanticAnalyzer.evaluate(ret);
		assertSame(AtomicType.SLIST, nullValue.getType());
		assertTrue(((SList) nullValue).isEmpty());

		Value value = environment.getValue("name");
		assertSame(AtomicType.DIGIT, value.getType());
		assertThat(((AtomicValue) value).getNativeValue(), is(equalTo("100")));

	}

}
