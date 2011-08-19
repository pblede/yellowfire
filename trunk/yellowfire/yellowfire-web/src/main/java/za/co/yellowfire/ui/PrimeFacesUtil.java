package za.co.yellowfire.ui;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.model.RequestResult;

/**
 * PrimeFaces utility methods
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class PrimeFacesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    /**
     * Adds the RequestResult to the RequestContext
     * @param result The request result to add
     */
    public static void addCallbackParam(RequestResult result) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            context.addCallbackParam("result", result);
        } else {
            LOGGER.warn("Cannot add callback parameter because request context is null");
        }
    }
}
