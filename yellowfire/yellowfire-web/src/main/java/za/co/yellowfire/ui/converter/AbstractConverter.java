package za.co.yellowfire.ui.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.Naming;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.faces.convert.Converter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Abstract converter that uses the DomainManager to lookup entities.
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractConverter implements Converter {
	protected static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONVERTER.getCategory());
	
	private DomainManager manager;

    /**
     * Default constructor
     */
    protected AbstractConverter() {
    }

    /**
     * Constructs the converter with the domain manager instance to use.
     * @param manager The domain manager
     */
    protected AbstractConverter(DomainManager manager) {
        this.manager = manager;
    }

    /**
     * Retrieves the domain manager. If the domain manager has not been set then it will be retrieved from JNDI
     * @return DomainManager
     * @throws NamingException If the domain manager could not be retrieved from JNDI
     */
    protected DomainManager getDomainManager() throws NamingException {
		if (manager == null) {
			manager = (DomainManager) new InitialContext().lookup(Naming.MANAGER_DOMAIN_JNDI_ENC);
		}
		return manager;
	}
}
