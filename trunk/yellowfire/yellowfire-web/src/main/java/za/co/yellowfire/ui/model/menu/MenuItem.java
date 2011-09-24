package za.co.yellowfire.ui.model.menu;

import za.co.yellowfire.ui.resources.MessageResources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menuitem", propOrder = {"messageId", "text", "url"})
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /*The message id in a resource bundle for the menu text*/
    @XmlAttribute(name = "text-id", required = false)
    private String messageId;

    /*The menu text*/
    @XmlAttribute(name = "text", required = false)
    private String text;

    /*The action url*/
    @XmlAttribute(name = "url", required = false)
    private String url;

    /* Default constructor */
    public MenuItem() { }

    /**
     * Constructs the menu item
     * @param text The message id in a resource bundle for the menu text
     * @param url The action url
     */
    public MenuItem(String text, String url) {
        this.text = text;
        this.url = url;
    }

    /**
     * Returns the message id of the menu text. The message id is the
     * key in a resource bundle to look up the message text
     * @return The message id of the menu item text
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message id of the menu text. The message id is the
     * key in a resource bundle to look up the message text
     * @param messageId The message id of the menu item text
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Returns the text of the menu item
     * @return Text
     */
    public String getText() {
        if (text != null) {
            return text;
        }
        text =  MessageResources.MESSAGE(this.messageId);
        return text;
    }

    /**
     * Sets the menu item text
     * @param text The new value
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the menu item action url
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the menu item action url
     * @param url The new value
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
