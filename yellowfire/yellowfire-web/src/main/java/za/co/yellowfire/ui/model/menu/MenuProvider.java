package za.co.yellowfire.ui.model.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.ui.annotation.*;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("MenuProvider")
public class MenuProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    private static final String MENU_COMMON = "/za/co/yellowfire/ui/model/menu/menu-common.xml";
    private static final String MENU_EMPLOYEE = "/za/co/yellowfire/ui/model/menu/menu-employee.xml";
    private static final String MENU_TRAINING = "/za/co/yellowfire/ui/model/menu/menu-training.xml";
    private static final String MENU_PERFORMANCE = "/za/co/yellowfire/ui/model/menu/menu-performance.xml";
    private static final String MENU_RACING = "/za/co/yellowfire/ui/model/menu/menu-racing.xml";

    private MenuManager manager = new MenuManager();
    private MenuModel commonMenu = null;
    private MenuModel employeeMenu = null;
    private MenuModel trainingMenu = null;
    private MenuModel performanceMenu = null;
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
    @Produces @Employee
    public MenuModel getEmployeeMenu() {
        if (employeeMenu == null) {
            try {
                employeeMenu = manager.read(MENU_EMPLOYEE);
            } catch (Exception e) {
                LOGGER.error("Unable to load menu " + MENU_EMPLOYEE, e);
            }
        }
        return employeeMenu;
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
     * Provides the menu for the performance sub-web
     * @return MenuModel
     */
    @Produces @Performance
    public MenuModel getPerformanceMenu() {
        if (performanceMenu == null) {
            try {
                performanceMenu = manager.read(MENU_PERFORMANCE);
            } catch (Exception e) {
                LOGGER.error("Unable to load menu " + MENU_PERFORMANCE, e);
            }
        }
        return performanceMenu;
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
