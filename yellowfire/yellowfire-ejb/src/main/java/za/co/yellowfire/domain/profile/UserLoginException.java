package za.co.yellowfire.domain.profile;

/**
 * @author Mark P Aashworth
 * @version 0.0.1
 */
public class UserLoginException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserLoginException() {
	}

	public UserLoginException(String message) {
		super(message);
	}

	public UserLoginException(Throwable cause) {
		super(cause);
	}

	public UserLoginException(String message, Throwable cause) {
		super(message, cause);
	}
}
