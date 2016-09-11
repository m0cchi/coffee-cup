package net.m0cchi.exception.handler;

public class Signal extends RuntimeException {
	private static final long serialVersionUID = -4227756806136995408L;

	public Signal() {
		super();
	}

	public Signal(String message) {
		super(message);
	}
}
