package net.m0cchi.remote.semantic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.m0cchi.parser.semantic.ISemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.NULL.NIL;

public class RemoteSemanticAnalyzer implements ISemanticAnalyzer {
	private String host;
	private int port;
	private Socket socket;

	public RemoteSemanticAnalyzer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws UnknownHostException, IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}

		socket = new Socket(host, port);
	}

	public void close() throws IOException {
		socket.close();
	}

	public Element evaluate(Element value) {
		Element ret = null;
		try {
			if (socket == null || !socket.isClosed()) {
				connect();
			}
			ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
			oos.writeObject(value);
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
			Object object = ois.readObject();
			if (object instanceof Element) {
				ret = (Element) object;
			} else {
				throw new Exception();
			}
		} catch (Throwable e) {
			ret = new NIL();
		}
		return ret;
	}
}
