package za.co.yellowfire.ui;

import za.co.yellowfire.ui.model.menu.MenuModel;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface UIController extends Serializable {
    /**
     * Returns the menu that is used by the controller
     * @return MenuModel
     */
    MenuModel getMenu();
}
