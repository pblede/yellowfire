package za.co.yellowfire.domain.profile;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class AuthenticationFailure implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private AuthenticationFailureType type;

    public AuthenticationFailure(User user, AuthenticationFailureType type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public AuthenticationFailureType getType() {
        return type;
    }
}
