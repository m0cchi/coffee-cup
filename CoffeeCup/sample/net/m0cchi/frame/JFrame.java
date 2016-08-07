package net.m0cchi.frame;

import net.m0cchi.function.Defvar;
import net.m0cchi.function.java.Invoke;
import net.m0cchi.function.java.New;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;

public class JFrame {
	public static void main(String[] args) {

		Environment environment = new Environment();
		environment.defineFunction(".", new Invoke());
		environment.defineFunction(".new", new New());
		environment.defineFunction("defvar", new Defvar());
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		StringLexicalAnalyzer lexicalAnalyser = new StringLexicalAnalyzer("(defvar frame (.new javax.swing.JFrame (\"Hello\")))"
				+ "(. setVisible frame (T))" + "(. setSize frame (400 600))");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyser);
		Element ret = syntaxAnalyzer.parse();
		Element[] values = ((SList) ret).toArray();
		for (Element value : values) {
			semanticAnalyzer.evaluate(value);
		}

	}
}
