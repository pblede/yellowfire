package za.co.yellowfire.domain.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManagerBean;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Local(UserManager.class)
//@Remote(UserManagerRemote.class)
@Stateless(name = "UserManager"/*, mappedName = "yellowfire/session/UserManager"*/)
public class UserManagerBean extends DomainManagerBean implements UserManager/*, UserManagerRemote*/ {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

	/**
     * Retrieves the user by name
     * @param name The name of the person
     * @return String
     */
	@SuppressWarnings("unchecked")
    @Override public User retrieve(String name) {    	
    	Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(User.FIELD_NAME, name);
     
        List<User> users = (List<User>) query(User.QRY_USER_NAME, params);
        if (users != null && users.size() > 0) {
        	return users.get(0);
        }
        return null;
    }
    
	/**
     * Performs a login for the supplied user
     * @param credential The credential to login
     * @return Whether the login was successful
     */
    @SuppressWarnings("unchecked")
    @Override public User login(Credential credential) {
    	
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(User.FIELD_NAME, credential.getName());
        params.put(User.FIELD_PASSWORD, credential.getPassword());
        
        List<User> users = (List<User>) query(User.QRY_USER_LOGIN, params);
        if (users != null && users.size() > 0) {
        	return users.get(0);
        }
        return null;
    }
    
    /**
     * Determines whether a username is available
     * @param userName To check for availability
     * @return True if the name is available else false
     */
    @SuppressWarnings("unchecked")
    @Override public boolean isUsernameAvailable(String userName) {	
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(User.FIELD_NAME, userName);
        
        List<User> users = (List<User>) query(User.QRY_USER_NAME, params);
        return users != null && users.size() == 0;
    }
    
    /**
     * Registers the user in the system
     * @param user The user to register
     * @return The registered user
     */
    @Override public User register(User user) throws UserRegistrationException {
    	
    	if (!isUsernameAvailable(user.getName())) {
    		throw new UserRegistrationException(String.format("The username %s is not available", user.getName()));
    	}
    	if (user.getPassword() == null) {
    		throw new UserRegistrationException("The password is required");
    	} else if (user.getPassword().equals("")) {
    		throw new UserRegistrationException("The password is required");
    	} else if (!user.getPassword().equals(user.getPasswordConfirmation())) {
    		throw new UserRegistrationException("The password confirmation should match the password");
    	}

    	super.persist(user);
        return user;
    }
    
    /**
     * Persists the user in the system
     * @param user The user to persist
     * @return The persisted user
     */
    @Override
    public User persist(User user) throws UserPersistException {
    	
    	
    	//User u = em.find(User.class, user.getId());
    	//if (u == null) {
    	//	throw new UserPersistException("The user is not a registered user");
    	//}
    	//em.refresh(u);
        
    	//final boolean passwordChanged = !u.getPassword().equals(user.getPassword());
    	//if (passwordChanged && !user.getPassword().equals(user.getPasswordConfirmation())) {
    	//	throw new UserPersistException("The password confirmation should match the password");
    	//}

        //LOGGER.info("Looking up the club : " + user.getClub());
//    	Club c = null;
//    	if (user.getClub() != null) {
//    		c = em.find(Club.class, user.getClub().getId());
//    	}

        LOGGER.info("Setting the persisted user info");
//    	u.setName(user.getName());
//    	u.setPassword(user.getPassword());
//    	u.setBirthDate(user.getBirthDate());
//    	u.setEmail(user.getEmail());
//    	u.setRole(user.getRole());
//    	u.setClub(c);
    	user.setEntityUpdated();

        LOGGER.info("Persisting user");
        return (User) super.merge(user);

        //return user;
    }

    /**
     * Verifies the user's profile
     * @param verificationKey The verification key to verify
     * @return The verified user
     * @throws UserPersistException If the user/s belonging to the key cannot be verified
     */
    @Override
    @SuppressWarnings("unchecked")
    public User verify(String verificationKey) throws UserPersistException {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(User.FIELD_VERIFICATION_KEY, verificationKey);

        List<User> users = (List<User>) query(User.QRY_USER_VERIFICATION_KEY, params);
        if (users != null && users.size() != 0) {
            User user = users.get(0);
            user.setVerified(true);
            return persist(user);
        }
        return null;
    }
}
