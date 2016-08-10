package net.m0cchi.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.m0cchi.function.Defvar;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.util.Program;
import net.m0cchi.value.AtomicType;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;
import net.m0cchi.value.Function;
import net.m0cchi.value.SList;
import net.m0cchi.value.Value;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	public static class HTTP_SERVER extends Function {
		private static final long serialVersionUID = -5810841397674551360L;

		public HTTP_SERVER() {
			setArgs(new String[] { "port number", REST, "contexts" });
		}

		public static class SListContext implements HttpHandler {
			SemanticAnalyzer semanticAnalyzer;
			Value<?> proc;

			public SListContext(Environment environment, Value<?> proc) {
				semanticAnalyzer = new SemanticAnalyzer(environment);
				this.proc = proc;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				Element element = semanticAnalyzer.evaluate(proc);
				Headers responseHeaders = exchange.getResponseHeaders();
				int status = 500;
				String body = null;
				if (element instanceof SList) {
					// 0: status
					// 1: header
					// last: body
					SList ret = (SList) element;
					Element[] values = ret.toArray();
					status = ((Value<Integer>) values[0]).getNativeValue();
					if (values.length == 2) {
						body = ((Value<String>) values[1]).getNativeValue();
					} else {
						body = ((Value<String>) values[2]).getNativeValue();
						SList headers = (SList) values[1];
						for (Element tmp : headers.toArray()) {
							SList header = (SList) tmp;
							// length == 2
							String name = ((Value<String>) header.get(0)).getNativeValue();
							String value = ((Value<String>) header.get(1)).getNativeValue();
							responseHeaders.add(name, value);
						}
					}
				} else {
					// body
					status = 200;
					body = ((Value<String>) element).getNativeValue();
				}
				byte[] bodyBytes = body.getBytes();
				exchange.sendResponseHeaders(status, bodyBytes.length);
				exchange.getResponseBody().write(bodyBytes);
			}
		}

		@Override
		public Element invoke(Environment environment) {
			@SuppressWarnings("unchecked")
			Value<Integer> portValue = (Value<Integer>) environment.getValue(getArgs()[0]);
			SList contexts = (SList) environment.getValue(getArgs()[2]);
			HttpServer server = null;
			try {
				server = HttpServer.create(new InetSocketAddress(portValue.getNativeValue()), 0);
				for (Element element : contexts.toArray()) {
					SList context = (SList) element;
					@SuppressWarnings("unchecked")
					Value<String> path = (Value<String>) context.get(0);
					Value<?> proc = (Value<?>) context.get(1);
					server.createContext(path.getNativeValue(), new SListContext(environment, proc));
					
				}
				server.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new Value<HttpServer>(AtomicType.JAVA, server);
		}
	}

	public static void main(String[] args) throws IOException {
		Program program = new Program("(defvar context '(\"/\" 'hello))"
				+ "(http-service 28080 context)");
		Environment environment = program.getEnvironment();
		environment.defineFunction("defvar", new Defvar());
		environment.defineFunction("http-service", new HTTP_SERVER());
		program.run();
	}

}
