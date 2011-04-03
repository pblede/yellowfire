package za.co.yellowfire.domain.profile;

import za.co.yellowfire.manager.DomainManagerBean;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Local(SystemManager.class)
@Stateless(name = "SystemManager", mappedName = "yellowfire/session/SystemManager")
public class SystemManagerBean extends DomainManagerBean implements SystemManager {

    /**
     * Returns the timezone that the system should be using
     * @return String
     */
    @Override
    public String getTimezone() {
        SystemProperty property = (SystemProperty) find(SystemProperty.class, SystemPropertyConfig.Timezone.getName());
        if (property != null) {
            return property.getValue();
        } else {
            return (String) SystemPropertyConfig.Timezone.getDefaultValue();
        }
    }
}
