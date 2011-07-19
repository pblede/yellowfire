package za.co.yellowfire.ui.racing;

import za.co.yellowfire.ui.UIController;
import za.co.yellowfire.ui.annotation.Racing;
import za.co.yellowfire.ui.model.menu.MenuModel;

import javax.inject.Inject;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class AbstractRacingUIController implements UIController {
    private static final long serialVersionUID = 1L;

    @Inject @Racing
    private MenuModel menu;

    /**
     * Returns the menu that is used by the controller
     * @return MenuModel
     */
    @Override
    public MenuModel getMenu() {
        return menu;
    }
}
