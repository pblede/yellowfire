package za.co.yellowfire.ui.converter;

import javax.faces.convert.Converter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.yellowfire.Naming;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;

public abstract class AbstractConverter implements Converter {
	protected static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONVERTER.getCategory());
	
	private DomainManager manager;
	
	protected DomainManager getDomainManager() throws NamingException {
		if (manager == null) {
			manager = (DomainManager) new InitialContext().lookup(Naming.MANAGER_DOMAIN);
		}
		return manager;
	}
}
