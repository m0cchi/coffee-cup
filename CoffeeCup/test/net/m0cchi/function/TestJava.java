package net.m0cchi.function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import net.m0cchi.function.Java.Invoke;
import net.m0cchi.function.Java.New;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.Test;

public class TestJava {

	@Test
	public void testNewString() {
		Environment environment = new Environment();
		environment.defineFunction(".new", new New());

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(.new java.lang.String (\"hello\"))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("\"hello\"")));
	}
	
	@Test
	public void testCallJavaMethod() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. toString (.new java.lang.String (\"hello\")) ()");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("\"hello\"")));
	}
	
	@Test
	public void testCallJavaMethod2() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. toString 100 ()");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("100")));
	}

}