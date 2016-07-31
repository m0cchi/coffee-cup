package net.m0cchi.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import net.m0cchi.parser.lexical.StringLexicalAnalyser;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSemanticAnalyzer {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluateValue() {
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
		StringLexicalAnalyser lexicalAnalyser = new StringLexicalAnalyser("1 ) \"hello\"");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Value ret = syntaxAnalyzer.parse();
		Value[] values = ((SList) ret).toArray();
		AtomicType[] expected = { AtomicType.DIGIT, AtomicType.RIGHT_PARENTHESIS, AtomicType.LETTER };
		int i = 0;
		for (Value value : values) {
			assertSame(expected[i++], semanticAnalyzer.evaluate(value).getType());
		}
		assertSame(expected.length, i);
	}

	@Test
	public void testEvaluateFunction() {
		StringLexicalAnalyser lexicalAnalyser = new StringLexicalAnalyser("(print \"hello\" 0 test-value)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Value ret = ((SList)syntaxAnalyzer.parse()).toArray()[0];
		Environment environment = new Environment();
		environment.defineVariable("test-value", new AtomicValue(AtomicType.DIGIT, "test-value"));
		environment.defineFunction("print", new Function() {
			{
				setArgs(new String[] { "test str", "test num", "test variable" });
			}

			@Override
			public Value invoke(Environment environment) {
				AtomicValue str = (AtomicValue) environment.getValue(getArgs()[0]);
				AtomicValue num = (AtomicValue) environment.getValue(getArgs()[1]);
				AtomicValue var = (AtomicValue) environment.getValue(getArgs()[2]);
				assertSame(AtomicType.LETTER, str.getType());
				assertThat(str.getNativeValue(), is(equalTo("\"hello\"")));
				assertSame(AtomicType.DIGIT, num.getType());
				assertThat(num.getNativeValue(), is(equalTo("0")));
				assertSame(AtomicType.DIGIT, var.getType());
				assertThat(var.getNativeValue(), is(equalTo("test-value")));
				return new AtomicValue(AtomicType.TERMINAL, "terminal");
			}
		});
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		Value value = semanticAnalyzer.evaluate(ret);
		assertSame(value.getType(), AtomicType.TERMINAL);
	}

}
