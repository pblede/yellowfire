package za.co.yellowfire.domain.profile;

import org.apache.catalina.Manager;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.manager.DomainManagerBean;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local(SystemManager.class)
@Stateless(name = "SystemManager", mappedName = "yellowfire/session/SystemManager")
public class SystemManagerBean implements SystemManager {

    @EJB private DomainManager manager;
    
    /**
     * Returns the timezone that the system should be using
     * @return String
     */
    @Override
    public String getTimezone() {
        SystemProperty property = (SystemProperty) manager.find(SystemProperty.class, SystemPropertyConfig.Timezone.getName());
        if (property != null) {
            return property.getValue();
        } else {
            return (String) SystemPropertyConfig.Timezone.getDefaultValue();
        }
    }
}
