package za.co.yellowfire.domain.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Local(UserManager.class)
//@Remote(UserManagerRemote.class)
@Stateless(name = "UserManager"/*, mappedName = "yellowfire/session/UserManager"*/)
public class UserManagerBean implements UserManager/*, UserManagerRemote*/ {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @EJB private DomainManager manager;

	/**
     * Retrieves the user by name
     * @param name The name of the person
     * @return String
     */
	@SuppressWarnings("unchecked")
    @Override public Profile retrieve(String name) {
    	Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Profile.FIELD_USER_NAME, name);
     
        List<Profile> users = (List<Profile>) manager.query(Profile.QRY_USER_NAME, params);
        if (users != null && users.size() > 0) {
        	return users.get(0);
        }
        return null;
    }
    
	/**
     * Performs a login for the supplied user
     * @param userName The user name to login
     * @param password The password to login
     * @return Whether the login was successful
     */
    @SuppressWarnings("unchecked")
    @Override public Profile login(String userName, String password) {
    	
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Profile.FIELD_USER_NAME, userName);
        params.put(Profile.FIELD_PASSWORD, password);
        
        List<Profile> users = (List<Profile>) manager.query(Profile.QRY_USER_LOGIN, params);
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
        params.put(Profile.FIELD_USER_NAME, userName);
        
        List<Profile> users = (List<Profile>) manager.query(Profile.QRY_USER_NAME, params);
        return users != null && users.size() == 0;
    }
    
    /**
     * Registers the user in the system
     * @param user The user to register
     * @return The registered user
     */
    @Override public Profile register(Profile user) throws UserRegistrationException {
    	
    	if (!isUsernameAvailable(user.getUserName())) {
    		throw new UserRegistrationException(String.format("The username %s is not available", user.getName()));
    	}
    	if (user.getPassword() == null) {
    		throw new UserRegistrationException("The password is required");
    	} else if (user.getPassword().equals("")) {
    		throw new UserRegistrationException("The password is required");
    	} else if (!user.getPassword().equals(user.getPasswordConfirmation())) {
    		throw new UserRegistrationException("The password confirmation should match the password");
    	}

    	manager.persist(user);
        return user;
    }
    
    /**
     * Persists the user in the system
     * @param user The user to persist
     * @return The persisted user
     */
    @Override
    public Profile persist(Profile user) throws UserPersistException {
    	
    	
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
        return (Profile) manager.merge(user);

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
    public Profile verify(String verificationKey) throws UserPersistException {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Profile.FIELD_VERIFICATION_KEY, verificationKey);

        List<Profile> users = (List<Profile>) manager.query(Profile.QRY_USER_VERIFICATION_KEY, params);
        if (users != null && users.size() != 0) {
            Profile user = users.get(0);
            user.setVerified(true);
            return persist(user);
        }
        return null;
    }
}
