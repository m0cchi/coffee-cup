package net.m0cchi.parser.lexical;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class StreamLexicalAnalyzer extends AbstractLexicalAnalyzer {
	private InputStream is;
	private LinkedList<Integer> stack;

	public StreamLexicalAnalyzer(InputStream is) {
		this.is = new BufferedInputStream(is);
		this.stack = new LinkedList<>();
	}
	
	@Override
	protected int read() {
		if(stack.size() > 0) {
			return this.stack.pop();
		}
		try {
			return this.is.read();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	protected void unread(int code) {
		this.stack.push(code);
	}

}
