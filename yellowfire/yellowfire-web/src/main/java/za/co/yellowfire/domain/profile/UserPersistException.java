package za.co.yellowfire.domain.profile;

/**
 * @author Mark P Aashworth
 * @version 0.0.1
 */
public class UserPersistException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserPersistException() {
	}

	public UserPersistException(String message) {
		super(message);
	}

	public UserPersistException(Throwable cause) {
		super(cause);
	}

	public UserPersistException(String message, Throwable cause) {
		super(message, cause);
	}
}
