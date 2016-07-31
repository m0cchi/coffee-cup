package net.m0cchi.parser.lexical;


public abstract class AbstractLexicalAnalyzer {
	
	/**
	 * read source code
	 * @return
	 */
	protected abstract int read();
	/**
	 * pushback char code
	 * @param code 0-255
	 */
	protected abstract void unread(int code);

}
