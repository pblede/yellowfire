package za.co.yellowfire.ui.model.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.annotation.Common;
import za.co.yellowfire.ui.annotation.Racing;
import za.co.yellowfire.ui.annotation.Training;
import za.co.yellowfire.ui.model.menu.MenuItem;
import za.co.yellowfire.ui.model.menu.MenuModel;
import za.co.yellowfire.ui.model.menu.SubMenu;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.ResourceBundle;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("MenuProvider")
public class MenuProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    private static final String MENU_COMMON = "/za/co/yellowfire/ui/model/menu/menu-common.xml";
    private static final String MENU_TRAINING = "/za/co/yellowfire/ui/model/menu/menu-training.xml";
    private static final String MENU_RACING = "/za/co/yellowfire/ui/model/menu/menu-racing.xml";

    private MenuManager manager = new MenuManager();
    private MenuModel commonMenu = null;
    private MenuModel trainingMenu = null;
    private MenuModel racingMenu = null;

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

    /**
     * Provides the menu for the training sub-web
     * @return MenuModel
     */
    @Produces @Training
    public MenuModel getTrainingMenu() {
        if (trainingMenu == null) {
            try {
                trainingMenu = manager.read(MENU_TRAINING);
            } catch (Exception e) {
                LOGGER.error("Unable to load menu " + MENU_TRAINING, e);
            }
        }
        return trainingMenu;
    }

    /**
     * Provides the menu for the racing sub-web
     * @return MenuModel
     */
    @Produces @Racing
    public MenuModel getRacingMenu() {
        if (racingMenu == null) {
            try {
                racingMenu = manager.read(MENU_RACING);
            } catch (Exception e) {
                LOGGER.error("Unable to load menu " + MENU_RACING, e);
            }
        }
        return racingMenu;
    }
}
