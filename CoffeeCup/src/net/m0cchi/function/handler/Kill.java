package net.m0cchi.function.handler;

import net.m0cchi.exception.handler.Abort;
import net.m0cchi.exception.handler.Signal;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.Value;

public class Kill extends Function {
	private static final long serialVersionUID = -7709583677888529462L;

	public Kill() {
		setArgs("kill target");
	}

	@Override
	public Element invoke(Environment environment) {
		Value<?> targetValue = environment.getValue(getArgs()[0]);
		Object target = targetValue.getNativeValue();
		if (target instanceof Signal) {
			throw (Signal) target;
		} else {
			try {
				Class<?> clazz = Class.forName(target.toString());
				throw (Signal) clazz.newInstance();
			} catch (Exception e) {
				throw new Abort();
			}
		}
	}

}
