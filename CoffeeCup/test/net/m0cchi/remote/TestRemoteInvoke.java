package net.m0cchi.remote;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import net.m0cchi.parser.lexical.AbstractLexicalAnalyzer;
import net.m0cchi.parser.lexical.StringLexicalAnalyzer;
import net.m0cchi.parser.syntax.SyntaxAnalyzer;
import net.m0cchi.remote.semantic.RemoteSemanticAnalyzer;
import net.m0cchi.remote.server.InvokeServer;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import org.junit.Test;

public class TestRemoteInvoke {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws IOException {
		InvokeServer server = null;
		try {
			Environment environment = new Environment();
			environment.defineFunction("greeting", new Function() {
				private static final long serialVersionUID = 1L;

				{
					setArgs(new String[] { "your name" });
				}

				@Override
				public Element invoke(Environment environment) {
					Value<String> nameValue = (Value<String>) environment.getValue(getArgs()[0]);
					String value = "hello, " + nameValue.getNativeValue();
					return new Value<String>(AtomicType.LETTER, value);
				}
			});
			server = new InvokeServer(9999);
			server.setDaemon(true);
			server.setEnvironment(environment);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(server);

		AbstractLexicalAnalyzer lexicalAnalyzer = new StringLexicalAnalyzer("(greeting 'm0cchi)");
		SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
		SList result = (SList) syntaxAnalyzer.parse();
		RemoteSemanticAnalyzer semanticAnalyzer = new RemoteSemanticAnalyzer("localhost", 9999);
		semanticAnalyzer.connect();
		Element ret = semanticAnalyzer.evaluate(result.get(0));

		assertSame(AtomicType.LETTER, ret.getType());
		assertThat(((Value<String>) ret).getNativeValue(), is(equalTo("hello, m0cchi")));

		semanticAnalyzer.close();
		server.close();
	}

}
