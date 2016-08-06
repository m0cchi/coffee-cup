package net.m0cchi.parser.lexical;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;
import net.m0cchi.value.Element;

public abstract class AbstractLexicalAnalyzer {
	/**
	 * END OF SOURCE CODE
	 */
	private final static int EOF = -1;
	private final static int DOT;
	private final static Map<Integer, AtomicType> PARENTHESIS_MAP = new HashMap<>();
	private final static Map<Integer, AtomicType> SIGN_MAP = new HashMap<>();
	private final static List<Integer> SKIP_LIST = new ArrayList<>();
	private final static List<Integer> DIGIT_LIST = new ArrayList<>();
	private final static List<Integer> LETTER_LIST = new ArrayList<>();
	private final LinkedList<Element> stack;

	{
		// initializer
		this.stack = new LinkedList<>();
	}

	static {
		init();
		DOT = toAsciiCode(".");
	}

	private static int toAsciiCode(String character) {
		return character.getBytes()[0];
	}

	private static void init() {
		// init sign
		PARENTHESIS_MAP.putIfAbsent(toAsciiCode("("), AtomicType.LEFT_PARENTHESIS);
		PARENTHESIS_MAP.putIfAbsent(toAsciiCode(")"), AtomicType.RIGHT_PARENTHESIS);
		SIGN_MAP.putAll(PARENTHESIS_MAP);
		SIGN_MAP.putIfAbsent(toAsciiCode("'"), AtomicType.QUOTE);
		// init skip
		SKIP_LIST.add(toAsciiCode("\n"));
		SKIP_LIST.add(toAsciiCode(" "));
		// init number
		for (String code : "1,2,3,4,5,6,7,8,9,0".split(",")) {
			DIGIT_LIST.add(toAsciiCode(code));
		}
		// init letter
		for (String code : "'\"".split("")) {
			LETTER_LIST.add(toAsciiCode(code));
		}
	}

	/**
	 * read source code <br>
	 * if returnValue is -1, fails to read
	 * 
	 * @return -1-255
	 */
	protected abstract int read();

	/**
	 * push back char code
	 * 
	 * @param code
	 *            0-255
	 */
	protected abstract void unread(int code);

	/**
	 * take a AtomicValue out of source code
	 * 
	 * @return AtomicValue
	 */
	public Element take() {
		if (this.stack.size() > 0) {
			return this.stack.pop();
		}
		return parser();
	}
	
	/**
	 * 
	 * @param value
	 */
	public void push(Element value) {
		this.stack.push(value);
	}

	private AtomicValue parseDigit() {
		int dotCounter = 0;
		int code;
		AtomicValue value = null;
		StringBuilder sb = new StringBuilder();
		while ((code = read()) != EOF) {
			if (!(DIGIT_LIST.contains(code) || code == DOT)) {
				unread(code);
				break;
			}

			if (code == DOT && dotCounter++ == 1) {
				// TODO: throw exception
				break;
			}
			sb.append((char) code);
		}

		if (dotCounter != 2) {
			value = new AtomicValue(AtomicType.DIGIT, sb.toString());
		} else {
			value = new AtomicValue(AtomicType.TERMINAL, sb.toString());
		}

		return value;
	}

	private AtomicValue parseSymbol() {
		AtomicValue value;
		int code;
		StringBuilder sb = new StringBuilder();
		while ((code = read()) != EOF) {
			if (SKIP_LIST.contains(code) || PARENTHESIS_MAP.containsKey(code)) {
				unread(code);
				break;
			}
			sb.append((char) code);
		}
		value = new AtomicValue(AtomicType.SYMBOL, sb.toString());
		return value;
	}

	private AtomicValue parseLetter(int period) {
		AtomicValue value = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int code;
		baos.write(period); 
		while (!((code = read()) == EOF || code == period)) {
			baos.write(code); 
		} 
		baos.write(period); 
		value = new AtomicValue(AtomicType.LETTER, baos.toString());
		return value;
	}
	
	/**
	 * source code parser
	 * 
	 * @return AtomicValue
	 */
	private AtomicValue parser() {
		int code;
		AtomicValue value = null;

		while ((code = read()) != EOF) {

			if (SKIP_LIST.contains(code)) {
				continue;
			} else if (SIGN_MAP.containsKey(code)) {
				value = new AtomicValue(SIGN_MAP.get(code), null);
				break;
			} else if (DIGIT_LIST.contains(code)) {
				unread(code);
				value = parseDigit();
				break;
			} else if (LETTER_LIST.contains(code)) {
				value = parseLetter(code);
				break;
			} else {
				unread(code);
				value = parseSymbol();
				break;
			}

		}

		if (code == EOF) {
			value = new AtomicValue(AtomicType.TERMINAL, null);
		}

		return value;
	}

}
