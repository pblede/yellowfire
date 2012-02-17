package za.co.yellowfire.ui;

/**
 * A interface that the controller supports the standard conversation scope methods to start and stop a conversation.
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface UIConversationController {
    /**
     * Whether the controller is in a conversation
     * @return Whether there is a conversation
     */
    boolean isInConversation();

    /**
     * Starts the conversation and returns the page to redirect to
     * @return The page / outcome / view to redirect to
     */
    String onStartConversation();

    /**
     * Completes the conversation
     * @return The page / outcome / view to redirect to
     */
    String onCompleteConversation();
}
