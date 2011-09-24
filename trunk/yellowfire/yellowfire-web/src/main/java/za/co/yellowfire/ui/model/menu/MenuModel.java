package za.co.yellowfire.ui.model.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@XmlRootElement(name="menu")
public class MenuModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger("MenuModel");

    /*The sub menus*/
    @XmlElement(name= "submenu")
    private List<SubMenu> subMenus = new ArrayList<SubMenu>();

    /**
     * Add a sub menu to the menu
     * @param subMenu The sub menu
     */
    public void addSubMenu(SubMenu subMenu) {
        this.subMenus.add(subMenu);
    }

    /**
     * Returns the list of sub menus
     * @return The list of sub menus
     */
    public List<SubMenu> getSubMenus() {
        return this.subMenus;
    }
}
