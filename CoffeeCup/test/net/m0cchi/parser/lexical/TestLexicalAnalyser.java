package net.m0cchi.parser.lexical;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLexicalAnalyser {

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
	public void testSign() {
		AbstractLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyser("()");
		AtomicType[] expected = { AtomicType.LEFT_PARENTHESIS, AtomicType.RIGHT_PARENTHESIS };
		AtomicValue value = null;
		int i = 0;
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			assertSame(expected[i++], value.getType());
		}
		assertSame(expected.length, i);
	}

	@Test
	public void testDigit() {
		String[] expected = { "123456789", "123.456", "0000000" };
		AbstractLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyser(String.join(" ", expected));
		AtomicValue value = null;
		int i = 0;
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			assertThat(value.getNativeValue(), is(equalTo(expected[i++])));
		}
		assertSame(expected.length, i);
	}

	@Test
	public void testSymbol() {
		String[] expected = { "hello", "hoge123.456.asd", "!a", "#a", "$a", "%a", "&a", "a" };
		AbstractLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyser(String.join(" ", expected));
		AtomicValue value = null;
		int i = 0;
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			assertThat(value.getNativeValue(), is(equalTo(expected[i++])));
		}
		assertSame(expected.length, i);

		lexicalAnalyzer = new StringLexicalAnalyser("hello)");
		AtomicType[] expected2 = { AtomicType.SYMBOL, AtomicType.RIGHT_PARENTHESIS };
		i = 0;
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			assertThat(value.getType(), is(equalTo(expected2[i++])));
			if (value.getType() == AtomicType.SYMBOL) {
				assertThat(value.getNativeValue(), is(equalTo("hello")));
			}
		}

		assertSame(expected2.length, i);
	}
	

	@Test
	public void testLetter() {
		String[] expected = { "\"he(l)''''' \nlo\"" };
		AbstractLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyser(String.join(" ", expected));
		AtomicValue value = null;
		int i = 0;
		while ((value = lexicalAnalyzer.take()) != null && value.getType() != AtomicType.TERMINAL) {
			assertThat(value.getNativeValue(), is(equalTo(expected[i++])));
		}
		assertSame(expected.length, i);
		
	}

}
