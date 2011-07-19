package za.co.yellowfire.ui.model.menu;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String messageId;
    private String message;
    private String url;

    public MenuItem(String message, String url) {
        this.message = message;
        this.url = url;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
