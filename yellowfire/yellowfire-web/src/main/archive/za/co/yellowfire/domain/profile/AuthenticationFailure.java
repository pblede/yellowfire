package za.co.yellowfire.domain.profile;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class AuthenticationFailure implements Serializable {
    private static final long serialVersionUID = 1L;

    private Credentials credential;
    private AuthenticationFailureType type;

    public AuthenticationFailure(Credentials credential, AuthenticationFailureType type) {
        this.credential = credential;
        this.type = type;
    }

    public Credentials getCredential() {
        return credential;
    }

    public AuthenticationFailureType getType() {
        return type;
    }
}
