package za.co.yellowfire.solarflare.web.model.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.solarflare.web.annotation.Common;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("MenuProvider")
public class MenuProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger("MenuProvider");

    private static final String MENU_COMMON = "/za/co/yellowfire/solareflare/web/model/menu/menu-common.xml";

    private MenuManager manager = new MenuManager();
    private MenuModel commonMenu = null;

    /**
     * Provides the menu for the common sub-web
     * @return MenuModel
     */
    @Produces @Common
    public MenuModel getCommonMenu() {
        if (commonMenu == null) {
            try {
                commonMenu = manager.read(MENU_COMMON);
            } catch (Exception e) {
                LOGGER.error("Unable to load menu " + MENU_COMMON, e);
            }
        }
        return commonMenu;
    }
}
