package za.co.yellowfire.domain.profile;
import javax.ejb.Local;


@Local
public interface UserManager {
	/**
     * Performs a login for the supplied user
     * @param userName The user name to login
     * @param password The password to login
     * @return Whether the login was successful
     */
    Profile login(String userName, String password);
    
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
    Profile register(Profile user) throws UserRegistrationException;
    
    /**
     * Persists the user in the system
     * @param user The user to persist
     * @return The persisted user
     */
    Profile persist(Profile user) throws UserPersistException;
    
    /**
     * Retrieves the user by name
     * @param name The name of the person
     * @return String
     */
    Profile retrieve(String name);

    /**
     * Verifies the user's profile
     * @param verificationKey The verification key to verify
     * @returns The verified user
     * @throws UserPersistException If the user/s belonging to the key cannot be verified
     */
    Profile verify(String verificationKey) throws UserPersistException;
}
