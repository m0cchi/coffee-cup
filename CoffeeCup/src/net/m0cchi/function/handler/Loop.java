package net.m0cchi.function.handler;

import net.m0cchi.exception.handler.LoopInterrupt;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Macro;
import net.m0cchi.value.SList;

public class Loop extends Macro {
	private static final long serialVersionUID = -2597737838672599632L;

	public Loop() {
		setArgs(REST, "loop body");
	}

	@Override
	public Element invoke(Environment environment) {
		SList body = (SList) environment.getValue(getArgs()[1]);
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(environment);
		Element ret = null;
		body.add(0, new DoList());// insert head
		while (true) {
			try {
				ret = semanticAnalyzer.evaluate(body);
			} catch (LoopInterrupt e) {
				break;
			}
		}
		return ret;
	}

}
