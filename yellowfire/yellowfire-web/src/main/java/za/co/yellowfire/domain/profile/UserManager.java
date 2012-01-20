package za.co.yellowfire.domain.profile;
import javax.ejb.Local;


@Local
public interface UserManager {
	/**
     * Performs a login for the supplied user
     * @param credential The credential to login
     * @return Whether the login was successful
     */
    User login(Credential credential);
    
    /**
     * Determines whether a username is available
     * @param userName To check for availability
     * @return True if the name is available else false
     */
    boolean isUsernameAvailable(String userName);
    
    /**
     * Registers the user in the system
     * @param user The user to register
     * @return The registered user
     */
    User register(User user) throws UserRegistrationException;
    
    /**
     * Persists the user in the system
     * @param user The user to persist
     * @return The persisted user
     */
    User persist(User user) throws UserPersistException;
    
    /**
     * Retrieves the user by name
     * @param name The name of the person
     * @return String
     */
    User retrieve(String name);

    /**
     * Verifies the user's profile
     * @param verificationKey The verification key to verify
     * @returns The verified user
     * @throws UserPersistException If the user/s belonging to the key cannot be verified
     */
    User verify(String verificationKey) throws UserPersistException;
}
