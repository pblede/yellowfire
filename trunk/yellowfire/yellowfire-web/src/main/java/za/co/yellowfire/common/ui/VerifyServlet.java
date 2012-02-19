package za.co.yellowfire.common.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.domain.profile.UserManager;
import za.co.yellowfire.domain.profile.UserPersistException;
import za.co.yellowfire.domain.profile.Verified;
import za.co.yellowfire.common.log.LogType;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO Refactor the verification into a component that can be Unit Tested
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@WebServlet(urlPatterns = "/verify", name = "VerifyServlet")
public class VerifyServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    
    @EJB
    private UserManager manager;

    @Inject @Verified
    private Event<Profile> verifiedEventSrc;

    /**
     * Verifies the user using the user verification key that was sent to the user via email.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there was an exception
     * @throws IOException If there was an exception
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String key = request.getParameter("key");
        if (key != null && !key.equals("")) {
            try {
                Profile user = manager.verify(key);
                if (user != null) {
                    verifiedEventSrc.fire(user);
                    response.sendRedirect("./index.jsf");
                } else {
                    /* No user found for the verification key ;-?*/
                    response.sendRedirect("./pages/error.jsf");
                }

            } catch (UserPersistException e) {
                LOGGER.error("Verification failure", e);
                response.sendRedirect("./pages/error.jsf");
            }
        } else {
            LOGGER.debug("VerifyServlet.doGet : No key ");
            response.sendRedirect("./pages/error.jsf");
        }
    }
}
