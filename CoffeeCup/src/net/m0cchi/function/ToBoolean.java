package net.m0cchi.function;

import java.util.List;

import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.NULL;
import net.m0cchi.value.Value;

public class ToBoolean extends Function {
	private static final long serialVersionUID = -2591031166407659551L;

	public ToBoolean() {
		setArgs("to boolean");
	}

	public static boolean isTrue(Element element) {
		if (element instanceof Value<?>) {
			Object ret = ((Value<?>) element).getNativeValue();
			if (ret instanceof NULL) {
				return false;
			}
			if (ret == null) {
				return false;
			}
			if (ret instanceof String) {
				return ((String) ret).length() > 0;
			}
			if (ret instanceof Integer) {
				return ((int) ret) != 0;
			}
			if (ret instanceof Boolean) {
				return (Boolean) ret;
			}
			if (ret instanceof List) {
				return ((List<?>) ret).size() > 0;
			}
			return true;
		}
		return false;
	}

	@Override
	public Element invoke(Environment environment) {
		Element obj = environment.getValue(getArgs()[0]);
		Value<Boolean> ret = new Value<>(AtomicType.BOOL, isTrue(obj));
		return ret;
	}

}
