package net.m0cchi.function.handler;

import java.util.ArrayList;
import java.util.List;

import net.m0cchi.function.ToBoolean;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

public class If extends Macro {
	private static final long serialVersionUID = 5802597784137772232L;

	public If() {
		setArgs(DEFAULT_NIL, "if cond", "if true", "if false");
	}

	@Override
	public Element invoke(Environment environment) {
		Element condValue = environment.getValue(getArgs()[1]);
		Element trueBody = environment.getValue(getArgs()[2]);
		Element falseBody = environment.getValue(getArgs()[3]);
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		List<Element> condList = new ArrayList<>();
		condList.add(new ToBoolean());
		condList.add(condValue);
		Element condResult = semanticAnalyzer.evaluate(condList);
		Element ret = null;
		if (condResult instanceof Value && ToBoolean.isTrue(condResult)) {
			ret = trueBody;
		} else {
			ret = falseBody == null ? new SList() : falseBody;
		}
		return ret;
	}

}
