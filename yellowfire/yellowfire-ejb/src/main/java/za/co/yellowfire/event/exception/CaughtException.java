package za.co.yellowfire.event.exception;

import java.io.Serializable;

public class CaughtException<T extends Throwable> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private T e;

	public CaughtException(T t) {
		super();
		this.e = t;
	}

	public T getException() {
		return e;
	}
}
