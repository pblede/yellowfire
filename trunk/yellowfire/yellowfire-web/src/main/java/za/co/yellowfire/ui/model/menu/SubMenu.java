package za.co.yellowfire.ui.model.menu;

import za.co.yellowfire.domain.geocode.GeocodeResult;
import za.co.yellowfire.ui.resources.MessageResources;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The sub menu in a menu
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submenu", propOrder = {"messageId", "text", "menuItems"})
public class SubMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /*The message id in a resource bundle for the menu text*/
    @XmlAttribute(name = "text-id", required = false)
    private String messageId;

    /*The menu text*/
    @XmlAttribute(name = "text", required = false)
    private String text;

    /*The list of menu items of a sub menu*/
    @XmlElement(name = "menuitem", type = MenuItem.class)
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();

    /* Default constructor */
    public SubMenu() { }

    /**
     * Constructs the menu item with the text
     * @param text The menu item text
     */
    public SubMenu(String text) {
        this.text = text;
    }

    /**
     * Returns the message id of the menu text. The message id is the
     * key in a resource bundle to look up the message text
     * @return The message id of the sub menu text
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message id of the menu text. The message id is the
     * key in a resource bundle to look up the message text
     * @param messageId The message id of the sub menu text
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        if (text != null) {
            return text;
        }
        text =  MessageResources.MESSAGE(this.messageId);
        return text;
    }

    /**
     * Sets the menu item text
     * @param text The sub menu text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Adds a menu item
     * @param menuItem The sub menu to add
     */
    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }

    /**
     * Returns the menu items
     * @return The list of menu items
     */
    public List<MenuItem> getMenuItems() {
        return this.menuItems;
    }
}
