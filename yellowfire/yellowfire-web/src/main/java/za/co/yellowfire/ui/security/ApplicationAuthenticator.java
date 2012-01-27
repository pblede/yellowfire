package za.co.yellowfire.ui.security;

import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.*;
import za.co.yellowfire.domain.profile.Profile;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("authenticator")
@ApplicationScoped
public class ApplicationAuthenticator extends BaseAuthenticator implements Authenticator {
   private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationAuthenticator.class);

    private AuthenticationStatus status;

    private org.picketlink.idm.api.User user;

    @EJB
    private UserManager userManager;

    @Inject
    private Credentials credentials;

    @Inject
    private Identity identity;

    /**
     * Authenticates the user
     */
    public void authenticate() {
        setStatus(AuthenticationStatus.FAILURE);

        if ((credentials.getUsername() == null) || (credentials.getCredential() == null)) {
            setStatus(AuthenticationStatus.FAILURE);
        } else if (credentials.getCredential() instanceof PasswordCredential) {
            PasswordCredential password = (PasswordCredential) credentials.getCredential();

            Profile user = userManager.login(credentials.getUsername(), password.getValue());

            if ((user != null) && user.getPassword().equals(password.getValue())) {
                setStatus(AuthenticationStatus.SUCCESS);
                setUser(user);
            } else {
                setStatus(AuthenticationStatus.FAILURE);
            }
        }
    }

    public void postAuthenticate() {
    }
}
