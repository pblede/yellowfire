package za.co.yellowfire.ui.resources;

import java.util.ResourceBundle;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class MessageResources {
    public static final String RESOURCE_BUNDLE_MESSAGES = "za.co.yellowfire.ui.resources.messages";
    private static ResourceBundle RB =  ResourceBundle.getBundle(MessageResources.RESOURCE_BUNDLE_MESSAGES);

    public static final String MESSAGE(MessageKey key) {
        String value = RB.getString(key.getKey());
        return value != null ? value : "??" + key.getKey() + "??";
    }

    public static final String ERROR_USER_LOGIN = "controller.user.login.error";
	public static final String ERROR_USER_PERSIST = "controller.user.persist.error";
	public static final String ERROR_USER_REGISTER = "controller.user.register.error";

	public static final String WARNING_USER_NOT_FOUND = "controller.user.not.found";
	public static final String WARNING_USER_NOT_VERIFIED = "controller.user.not.verified";

    public static final String INFO_USER_PERSISTED = "controller.user.persisted";
}
