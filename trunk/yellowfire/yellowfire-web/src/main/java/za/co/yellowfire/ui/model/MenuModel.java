package za.co.yellowfire.ui.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class MenuModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MODEL.getCategory());

    private List<SubMenu> subMenus = new ArrayList<SubMenu>();

    public void addSubMenu(SubMenu subMenu) {
        this.subMenus.add(subMenu);
    }

    public List<SubMenu> getSubMenus() {
        return this.subMenus;
    }
}
