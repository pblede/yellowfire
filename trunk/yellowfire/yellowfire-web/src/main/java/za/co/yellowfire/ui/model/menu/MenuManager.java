package za.co.yellowfire.ui.model.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * Manages the menu
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class MenuManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    /**
     * Reads the menu from a XML file as a resource from the current archive
     * @param resource The resource URL
     * @return The menu model
     * @throws JAXBException If the XML could not be parsed
     * @throws IOException If the XML resource could not be loaded
     */
    public MenuModel read(String resource) throws JAXBException, IOException {
        LOGGER.debug("Reading menu from resource {}", resource != null ? resource : "null");
        return read(this.getClass().getResource(resource));
    }

    /**
     * Reads the menu from a XML file as a resource url from the current archive
     * @param url The resource URL
     * @return The menu model
     * @throws JAXBException If the XML could not be parsed
     * @throws IOException If the XML resource could not be loaded
     */
    public MenuModel read(URL url) throws JAXBException, IOException {
        LOGGER.debug("Reading menu from url {}", url != null ? url.toString() : "null");
        if (url != null) {
            return read(url.openStream());
        }
        return null;
    }

    /**
     * Reads the menu from a XML file as an input stream
     * @param is The input stream of the XML
     * @return The menu model
     * @throws JAXBException If the XML could not be parsed
     */
    public MenuModel read(InputStream is) throws JAXBException {
        LOGGER.debug("Reading menu from input stream");

        Class[] classes = new Class[1];
        classes[0] = MenuModel.class;
        JAXBContext jc = JAXBContext.newInstance(classes);

        Unmarshaller u = jc.createUnmarshaller ();

        MenuModel item =  (MenuModel) u.unmarshal(is);
        for (SubMenu subMenu : item.getSubMenus()) {
            LOGGER.debug("{}:{}", subMenu.getMessageId(), subMenu.getText());
            for (MenuItem menuItem : subMenu.getMenuItems()) {
                LOGGER.debug("\t {}:{} {}", new Object[]{menuItem.getMessageId(), menuItem.getText(), menuItem.getUrl()});
            }
        }
        return item;
    }
}
