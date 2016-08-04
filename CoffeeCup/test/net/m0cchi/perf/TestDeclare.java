package net.m0cchi.perf;

import static org.junit.Assert.*;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Value;

import org.junit.Test;

public class TestDeclare {

	@Test
	public void test() {
		Function declareFunction = new Function() {
			{
				this.setArgs(new String[] { "declare args" });
			}

			@Override
			public Value invoke(Environment environment) {
				return environment.getValue(getArgs()[0]);
			}
		};
		Environment environment = new Environment();
		environment.defineFunction("declare", declareFunction);
		
		String code = "";
		StringLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyzer(code);
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			
		}
	}

}
