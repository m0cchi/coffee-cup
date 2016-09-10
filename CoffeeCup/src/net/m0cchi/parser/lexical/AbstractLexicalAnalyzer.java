package net.m0cchi.parser.lexical;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Value;

public abstract class AbstractLexicalAnalyzer {
	/**
	 * END OF SOURCE CODE
	 */
	protected final static int EOF = -1;
	protected final static int DOT;
	protected final static int NEW_LINE = toAsciiCode("\n");
	protected final static int SKIP_LINE = toAsciiCode(";");
	private final static Map<Integer, AtomicType> PARENTHESIS_MAP = new HashMap<>();
	private final static Map<Integer, AtomicType> SIGN_MAP = new HashMap<>();
	private final static List<Integer> SKIP_LIST = new ArrayList<>();
	private final static List<Integer> DIGIT_LIST = new ArrayList<>();
	private final static List<Integer> LETTER_LIST = new ArrayList<>();
	private final static List<Integer> BOOL_LIST = new ArrayList<>();
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
		SKIP_LIST.add(toAsciiCode("\r"));
		SKIP_LIST.add(toAsciiCode(" "));
		// init number
		for (String code : "1,2,3,4,5,6,7,8,9,0".split(",")) {
			DIGIT_LIST.add(toAsciiCode(code));
		}
		// init letter
		for (String code : "'\"".split("")) {
			LETTER_LIST.add(toAsciiCode(code));
		}
		// init bool
		BOOL_LIST.add(toAsciiCode("T"));
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
	 * take a Value out of source code
	 * 
	 * @return Value
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

	private Element parseDigit() {
		int dotCounter = 0;
		int code;
		Element value = null;
		StringBuilder sb = new StringBuilder();
		while ((code = read()) != EOF) {
			if (!(DIGIT_LIST.contains(code) || code == DOT)) {
				unread(code);
				break;
			}

			if (code == DOT && dotCounter++ == 1) {
				// TODO: throw exception
				value = new Value<String>(AtomicType.TERMINAL, sb.toString());
				break;
			}
			sb.append((char) code);
		}

		if (dotCounter == 0) {
			value = new Value<Integer>(AtomicType.DIGIT, Integer.parseInt(sb.toString()));
		} else if (dotCounter == 1) {
			value = new Value<Double>(AtomicType.DIGIT, Double.parseDouble(sb.toString()));
		}

		return value;
	}

	private Element parseSymbol() {
		Element value;
		int code;
		StringBuilder sb = new StringBuilder();
		while ((code = read()) != EOF) {
			if (SKIP_LIST.contains(code) || PARENTHESIS_MAP.containsKey(code)) {
				unread(code);
				break;
			}
			sb.append((char) code);
		}
		value = new Value<String>(AtomicType.SYMBOL, sb.toString());
		return value;
	}

	private Element parseLetter(int period) {
		Element value = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int code;
		while (!((code = read()) == EOF || code == period)) {
			baos.write(code);
		}
		value = new Value<String>(AtomicType.LETTER, baos.toString());
		return value;
	}

	/**
	 * source code parser
	 * 
	 * @return Value
	 */
	private Element parser() {
		int code;
		Element value = null;

		while ((code = read()) != EOF) {
			if (code == SKIP_LINE) {
				while ((code = read()) != EOF) {
					if (code == NEW_LINE) {
						break;
					}
				}
			} else if (SKIP_LIST.contains(code)) {
				continue;
			} else if (SIGN_MAP.containsKey(code)) {
				value = new Value<String>(SIGN_MAP.get(code), null);
				break;
			} else if (DIGIT_LIST.contains(code)) {
				unread(code);
				value = parseDigit();
				break;
			} else if (LETTER_LIST.contains(code)) {
				value = parseLetter(code);
				break;
			} else if (BOOL_LIST.contains(code)) {
				value = new Value<Boolean>(AtomicType.BOOL, true);
				break;
			} else {
				unread(code);
				value = parseSymbol();
				break;
			}

		}

		if (code == EOF) {
			value = new Value<String>(AtomicType.TERMINAL, null);
		}

		return value;
	}

}
