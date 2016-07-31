package net.m0cchi.parser.lexical;

import static org.junit.Assert.*;
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
		while ((value = lexicalAnalyzer.take()) != null) {
			assertSame(expected[i++], value.getType());
		}
		assertSame(expected.length, i);
	}

}
