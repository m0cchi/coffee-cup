package net.m0cchi.util;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.stream.IntStream;

import net.m0cchi.parser.lexical.StreamLexicalAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class Interpreter {
	public static String repeat(String str, int num) {
		StringBuilder builder = new StringBuilder();
		IntStream.range(0, num).forEach(nop -> builder.append(str));
		return builder.toString();
	}

	public static void printHello() {
		String name = "CoffeeCup Interpreter";
		int width = 40;
		System.out.println(repeat("*", width));
		System.out.println("*" + repeat(" ", width - 2) + "*");
		// centering
		System.out.println("*" + repeat(" ", (width - name.length()) / 2 - 1) + name + repeat(" ", (width - name.length()) / 2) + "*");
		System.out.println("*" + repeat(" ", width - 2) + "*");
		System.out.println("*" + repeat(" ", width - 2) + "*");
		System.out.println("*" + repeat(" ", width - 2 - "16.08.2016".length() - 1) + "16.08.2016" + " " + "*");
		System.out.println("*" + repeat(" ", width - 2) + "*");
		System.out.println(repeat("*", width));
	}

	public static void runCode(Environment environment) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		ByteArrayInputStream bais;
		StreamLexicalAnalyzer lexicalAnalyzer;
		SyntaxAnalyzer syntaxAnalyzer;
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		while (true) {
			System.out.print("> ");
			String code = in.nextLine();
			if("exit".equalsIgnoreCase(code)) {
				System.out.println("bye.");
				break;
			}
			
			bais = new ByteArrayInputStream(code.getBytes());
			lexicalAnalyzer = new StreamLexicalAnalyzer(bais);
			syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
			SList ret = (SList) syntaxAnalyzer.parse();
			for (Element element : ret.toArray()) {
				Element res = semanticAnalyzer.evaluate(element);
				if (res instanceof Value<?>) {
					System.out.println(((Value<?>) res).getNativeValue());
				} else {
					System.out.println(element.getType());
				}
			}

		}
	}
	/**
	 * Sample
	 * @param args
	 */
	public static void main(String... args) {
		printHello();
		Environment environment = new Environment();
		runCode(environment);
	}

}
