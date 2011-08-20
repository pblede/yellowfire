package za.co.yellowfire.ui.model.menu;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class MenuTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuTest.class);

    @Test
    public void testModel() {
        MenuModel model = new MenuModel();
        SubMenu subMenu = new SubMenu("Menu");
        model.addSubMenu(subMenu);
        subMenu.addMenuItem(new MenuItem("item 1", "url 1"));
        subMenu.addMenuItem(new MenuItem("item 2", "url 2"));

        Assert.assertEquals("Submenu text not equal", "Menu", model.getSubMenus().get(0).getText());
        Assert.assertEquals("Menuitem text not equal", "item 1", model.getSubMenus().get(0).getMenuItems().get(0).getText());
        Assert.assertEquals("Menuitem text not equal", "item 2", model.getSubMenus().get(0).getMenuItems().get(1).getText());

        Assert.assertEquals("Menuitem url not equal", "url 1", model.getSubMenus().get(0).getMenuItems().get(0).getUrl());
        Assert.assertEquals("Menuitem url not equal", "url 2", model.getSubMenus().get(0).getMenuItems().get(1).getUrl());

        MenuItem item = new MenuItem();
        item.setMessageId("company.name");
        item.setUrl("url 3");

        Assert.assertEquals("The loaded menuitem text not equal", "mp.ashworth", item.getText());
        Assert.assertEquals("The loaded url not equal", "url 3", item.getUrl());

        subMenu = new SubMenu();
        subMenu.setMessageId("company.name");

        Assert.assertEquals("The loaded submenu text not equal", "mp.ashworth", subMenu.getText());
    }

    @Test
    public void testParse() throws JAXBException, IOException {
        MenuManager manager = new MenuManager();
        MenuModel menu = manager.read("/za/co/yellowfire/ui/model/menu/menu-training.xml");

        Assert.assertTrue(menu.getSubMenus().size() > 0);
        Assert.assertTrue(menu.getSubMenus().get(0).getMenuItems().size() > 0);
    }

    @Test
    public void textProvider() {
        MenuProvider provider = new MenuProvider();
        MenuModel menu = provider.getCommonMenu();

        Assert.assertNotNull("The common menu is null");
        Assert.assertTrue("The common menu has no submenus", menu.getSubMenus().size() > 0);
        Assert.assertTrue("The common menu has no menuitems", menu.getSubMenus().get(0).getMenuItems().size() > 0);

        menu = provider.getTrainingMenu();

        Assert.assertNotNull("The training menu is null");
        Assert.assertTrue("The training menu has no submenus", menu.getSubMenus().size() > 0);
        Assert.assertTrue("The training menu has no menuitems", menu.getSubMenus().get(0).getMenuItems().size() > 0);

        menu = provider.getRacingMenu();

        Assert.assertNotNull("The racing menu is null");
        Assert.assertTrue("The racing menu has no submenus", menu.getSubMenus().size() > 0);
        Assert.assertTrue("The racing menu has no menuitems", menu.getSubMenus().get(0).getMenuItems().size() > 0);
    }
}
