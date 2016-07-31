package net.m0cchi.parser.lexical;

import static org.junit.Assert.*;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.xml.internal.ws.client.sei.ValueSetter;

public class TestSyntaxAnalyzer {

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
	public void testParse() {
		StringLexicalAnalyser lexicalAnalyser = new StringLexicalAnalyser("(+ 1 (+ (+ 2)) 3)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Value ret = syntaxAnalyzer.parse();
		Value[] values = ((SList) ret).toArray();
		assertSame(1, values.length); // top level slist

		SList slist = (SList) values[0];
		assertSame(6, slist.toArray().length);// count elements

		// check elements
		values = slist.toArray();
		AtomicType[] expected = { AtomicType.LEFT_PARENTHESIS, AtomicType.SYMBOL, AtomicType.DIGIT, AtomicType.SLIST, AtomicType.DIGIT,
				AtomicType.RIGHT_PARENTHESIS };
		int i = 0;
		for(Value value : values){
			assertSame(expected[i++], value.getType());
		}
		assertSame(expected.length, i);

	}

}
