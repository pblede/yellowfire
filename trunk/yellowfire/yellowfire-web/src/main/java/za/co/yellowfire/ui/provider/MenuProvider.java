package za.co.yellowfire.ui.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.ui.annotation.Common;
import za.co.yellowfire.ui.annotation.Racing;
import za.co.yellowfire.ui.annotation.Training;
import za.co.yellowfire.ui.model.menu.MenuItem;
import za.co.yellowfire.ui.model.menu.MenuModel;
import za.co.yellowfire.ui.model.menu.SubMenu;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.ResourceBundle;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("MenuProvider")
public class MenuProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONVERTER.getCategory());

    private ResourceBundle rb =  ResourceBundle.getBundle("za.co.yellowfire.ui.resources.messages");

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
            commonMenu = new MenuModel();

            SubMenu subMenu = new SubMenu(rb.getString("menu.administration"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.common.running"), "/running/index.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.common.training"),"/training/index.jsf"));
            commonMenu.addSubMenu(subMenu);
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
            trainingMenu = new MenuModel();
            SubMenu subMenu = new SubMenu(rb.getString("menu.administration.venue"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.venue.venues"), "/training/venues.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.venue.location.search"), "/training/location_search.jsf"));
            trainingMenu.addSubMenu(subMenu);

            subMenu = new SubMenu(rb.getString("menu.administration.training"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.training.outcomes"), "/training/outcomes.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.skill.areas"), "/training/skill_areas.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.training.courses"), "/training/courses.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.training.course.dates"), "/training/course_dates.jsf"));
            trainingMenu.addSubMenu(subMenu);

            subMenu = new SubMenu(rb.getString("menu.help"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.help.topics"), "/topics.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.help.about"), "/about.jsf"));
            trainingMenu.addSubMenu(subMenu);
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
            racingMenu = new MenuModel();

            SubMenu subMenu = new SubMenu(rb.getString("menu.administration"));
            racingMenu.addSubMenu(subMenu);

            subMenu = new SubMenu(rb.getString("menu.administration.running"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.running.races"), "/running/races.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.administration.running.result.calendar"), "/running/result_calendar.jsf"));
            racingMenu.addSubMenu(subMenu);
            
            subMenu = new SubMenu(rb.getString("menu.help"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.help.topics"), "/topics.jsf"));
            subMenu.addMenuItem(new MenuItem(rb.getString("menu.help.about"), "/about.jsf"));
            racingMenu.addSubMenu(subMenu);
        }
        return racingMenu;
    }
}
