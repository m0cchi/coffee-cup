package net.m0cchi.parser.lexical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.AtomicValue;

public abstract class AbstractLexicalAnalyzer {
	/**
	 * END OF SOURCE CODE
	 */
	private final static int EOF = -1;
	private final static Map<Integer, AtomicType> SIGN_MAP = new HashMap<>();
	private final static Map<Integer, AtomicType> SKIP_MAP = new HashMap<>();
	private final static List<Integer> DIGIT_LIST = new ArrayList<Integer>();

	static {
		init();
	}

	private static int toAsciiCode(String character) {
		return "(".getBytes()[0];
	}

	private static void init() {
		// init sign
		SIGN_MAP.putIfAbsent(toAsciiCode("("), AtomicType.LEFT_PARENTHESIS);
		SIGN_MAP.putIfAbsent(toAsciiCode(")"), AtomicType.RIGHT_PARENTHESIS);
		// init skip
		SKIP_MAP.putIfAbsent(toAsciiCode("\n"), AtomicType.RIGHT_PARENTHESIS);
		SKIP_MAP.putIfAbsent(toAsciiCode(" "), AtomicType.RIGHT_PARENTHESIS);
		// init number
		for (String num : "1,2,3,4,5,6,7,8,9,0".split(",")) {
			DIGIT_LIST.add(toAsciiCode(num));
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
	public AtomicValue take() {
		return parser();
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
			if (SKIP_MAP.containsKey(code)) {
				continue;
			} else if (SIGN_MAP.containsKey(code)) {
				value = new AtomicValue(SKIP_MAP.get(code), null);
			} else if(DIGIT_LIST.contains(code)) {
				continue;
			} else {
				continue;
			}
		}

		return value;
	}

}
