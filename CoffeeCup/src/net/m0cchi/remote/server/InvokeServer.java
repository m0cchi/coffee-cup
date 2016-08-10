package net.m0cchi.remote.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import net.m0cchi.parser.semantic.ISemanticAnalyzer;
import net.m0cchi.parser.semantic.SemanticAnalyzer;
import net.m0cchi.value.Element;
import net.m0cchi.value.Environment;

public class InvokeServer extends Thread {
	private ServerSocket server;
	private int port;
	private boolean canRun;
	private Environment environment;

	public InvokeServer(int port) throws IOException {
		server = new ServerSocket();
		server.setReuseAddress(true);
		this.port = port;
		this.environment = new Environment();
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void start() {
		try {
			canRun = true;
			server.bind(new InetSocketAddress(port));
			super.start();
		} catch (IOException e) {

		}
	}

	private class Connection implements Runnable {
		ISemanticAnalyzer semanticAnalyzer;
		Socket socket;

		public Connection(ISemanticAnalyzer semanticAnalyzer, Socket socket) {
			this.semanticAnalyzer = semanticAnalyzer;
			this.socket = socket;
		}

		@Override
		public void run() {
			while (!socket.isClosed()) {
				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

					Object object = ois.readObject();
					Element ret;
					if (object instanceof Element) {
						try {
							ret = semanticAnalyzer.evaluate((Element) object);
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							oos.writeObject(ret);
							oos.flush();
						} catch (Exception e) {
						}
					}
				} catch (Throwable e1) {
					e1.printStackTrace();
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void run() {
		Socket socket;
		SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(this.environment);
		List<Socket> sockets = new LinkedList<Socket>();
		try {
			while (canRun && (socket = server.accept()) != null) {
				Connection connection = new Connection(semanticAnalyzer, socket);
				new Thread(connection).start();
				sockets.add(socket);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			canRun = false;
		} finally {
			for (Socket sock : sockets) {
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		canRun = false;
	}

}
