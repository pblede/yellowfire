package za.co.yellowfire.ui.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named("ConverterProvider")
public class ConverterProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONVERTER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;
    
    @Produces
    @SuppressWarnings("unused")
    public RaceConverter getRaceConverter() {
        LOGGER.warn("ConverterProvider.getRaceConverter()");
        return new RaceConverter(manager);
    }

    @Produces
    @SuppressWarnings("unused")
    public ClubConverter getClubConverter() {
        LOGGER.warn("ConverterProvider.getClubConverter()");
        return new ClubConverter(manager);
    }
}
