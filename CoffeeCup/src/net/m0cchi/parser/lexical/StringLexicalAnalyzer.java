package net.m0cchi.parser.lexical;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringLexicalAnalyzer extends StreamLexicalAnalyzer {

	public StringLexicalAnalyzer(String str) {
		super(toInputStream(str));
	}

	private static InputStream toInputStream(String str) {
		ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
		return bais;
	}

}
