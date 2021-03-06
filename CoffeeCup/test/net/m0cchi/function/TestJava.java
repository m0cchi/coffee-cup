package net.m0cchi.function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import net.m0cchi.function.java.AppendLoadPath;
import net.m0cchi.function.java.GetField;
import net.m0cchi.function.java.Invoke;
import net.m0cchi.function.java.New;
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
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(.new java.lang.String \"hello\")");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("hello")));
	}

	@Test
	public void testCallJavaMethod() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. toString (.new java.lang.String \"hello\"))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("hello")));
	}

	@Test
	public void testCallJavaMethod2() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. toString 100)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("100")));
	}

	@Test
	public void testCast() throws Throwable {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer(
				"(. hoge (.new net.m0cchi.function.data.Super) (.new net.m0cchi.function.data.Sub))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("sub")));
	}

	@Test
	public void testOverride() throws Throwable {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. override (.new net.m0cchi.function.data.Sub))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("sub:override")));
	}

	@Test
	public void testOverload() throws Throwable {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer(
				"(. overload (.new net.m0cchi.function.data.Sub) (.new net.m0cchi.function.data.Sub))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("suboverload")));
	}

	@Test
	public void testOverload2() throws Throwable {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer(
				"(. overload (.new net.m0cchi.function.data.Sub) (.new net.m0cchi.function.data.Super))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("super")));
	}

	@Test
	public void testNotFound() throws Throwable {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. hige (.new net.m0cchi.function.data.Sub) 12 34)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		semanticAnalyzer.evaluate(values[0]);
	}

	@Test
	public void testNull() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(. notNull (.new net.m0cchi.function.data.Sub) nil)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		Element result = semanticAnalyzer.evaluate(values[0]);
		assertSame(AtomicType.JAVA, result.getType());
		@SuppressWarnings("unchecked")
		Value<String> object = (Value<String>) result;
		assertThat(object.getNativeValue(), is(equalTo("null")));
	}

	@Test
	public void testAppendLoadPath() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		environment.defineFunction("append-loadpath", new AppendLoadPath());

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(append-loadpath \"test-data/JavaLibrary.jar\")"
				+ "(. toString (.new JavaLibrary))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		
		semanticAnalyzer.evaluate(values[0]);
		
		@SuppressWarnings("unchecked")
		Value<String> result = (Value<String>) semanticAnalyzer.evaluate(values[1]);

		assertThat(result.getNativeValue(), is(equalTo("invoked")));	
	}

	@Test
	public void testGetField() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		environment.defineFunction(new GetField());
		environment.defineFunction(new Defvar());
		environment.defineFunction("append-loadpath", new AppendLoadPath());

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defvar super (.new net.m0cchi.function.data.Super))"
				+ "(get-field field (.new net.m0cchi.function.data.Super))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();

		semanticAnalyzer.evaluate(values[0]);
		
		@SuppressWarnings("unchecked")
		Value<String> result = (Value<String>) semanticAnalyzer.evaluate(values[1]);

		assertThat(result.getNativeValue(), is(equalTo("super")));	
	}
	
	@Test
	public void testGetPublicStaticField() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		environment.defineFunction(new GetField());
		environment.defineFunction(new Defvar());
		environment.defineFunction("append-loadpath", new AppendLoadPath());

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defvar super (.new net.m0cchi.function.data.Super))"
				+ "(get-field staticField super)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();

		semanticAnalyzer.evaluate(values[0]);
		
		@SuppressWarnings("unchecked")
		Value<String> result = (Value<String>) semanticAnalyzer.evaluate(values[1]);

		assertThat(result.getNativeValue(), is(equalTo("super-static")));	
	}
	
	@Test
	public void testBoxing() {
		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		environment.defineFunction(new GetField());
		environment.defineFunction(new Defvar());
		environment.defineFunction("append-loadpath", new AppendLoadPath());

		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defvar super (.new net.m0cchi.function.data.Super))"
				+ "(. boxing super 13)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();

		semanticAnalyzer.evaluate(values[0]);
		
		@SuppressWarnings("unchecked")
		Value<Integer> result = (Value<Integer>) semanticAnalyzer.evaluate(values[1]);

		assertThat(result.getNativeValue(), is(equalTo(26)));
	}
}
