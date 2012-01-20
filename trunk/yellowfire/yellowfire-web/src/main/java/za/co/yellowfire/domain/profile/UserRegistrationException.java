package za.co.yellowfire.domain.profile;

/**
 * @author Mark P Aashworth
 * @version 0.0.1
 */
public class UserRegistrationException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserRegistrationException() {
	}

	public UserRegistrationException(String message) {
		super(message);
	}

	public UserRegistrationException(Throwable cause) {
		super(cause);
	}

	public UserRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
