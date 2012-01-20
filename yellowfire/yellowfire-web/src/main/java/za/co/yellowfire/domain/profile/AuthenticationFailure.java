package za.co.yellowfire.domain.profile;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class AuthenticationFailure implements Serializable {
    private static final long serialVersionUID = 1L;

    private Credential credential;
    private AuthenticationFailureType type;

    public AuthenticationFailure(Credential credential, AuthenticationFailureType type) {
        this.credential = credential;
        this.type = type;
    }

    public Credential getCredential() {
        return credential;
    }

    public AuthenticationFailureType getType() {
        return type;
    }
}
