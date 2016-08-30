package net.m0cchi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.parser.lexical.StreamLexicalAnalyzer;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;

public class Program implements Runnable {
	private SyntaxAnalyzer syntaxAnalyzer;
	private SemanticAnalyzer semanticAnalyzer;
	private Environment environment;

	private Program() {
		this.environment = new Environment();
	}

	public Program(File file) throws FileNotFoundException {
		this();
		init(file);
	}

	public Program(String code) {
		this();
		init(code);
	}

	public void init(File file) throws FileNotFoundException {
		System.setProperty("user.dir", new File(file.getAbsolutePath()).getParent());
		init(new StreamLexicalAnalyzer(new FileInputStream(file)));
	}

	public void init(AbstractLexicalAnalyzer lexicalAnalyzer) {
		this.syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
		this.semanticAnalyzer = new SemanticAnalyzer(this.environment);
	}

	public void init(String code) {
		init(new StringLexicalAnalyzer(code));
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Element execute() {
		Element ret = null;
		SList values = (SList) syntaxAnalyzer.parse();
		for (Element value : values.getNativeValue()) {
			ret = this.semanticAnalyzer.evaluate(value);
		}
		return ret;
	}

	@Override
	public void run() {
		execute();
	}

}
