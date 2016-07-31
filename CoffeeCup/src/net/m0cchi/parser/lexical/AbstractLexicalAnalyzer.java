package net.m0cchi.parser.lexical;

import net.m0cchi.value.AtomicValue;

public abstract class AbstractLexicalAnalyzer {

	/**
	 * read source code
	 * 
	 * @return
	 */
	protected abstract int read();

	/**
	 * pushback char code
	 * 
	 * @param code
	 *            0-255
	 */
	protected abstract void unread(int code);

	/**
	 *  take a AtomicValue out of source code
	 * @return
	 */
	public AtomicValue take() {
		return null;
	}

}
