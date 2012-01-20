package za.co.yellowfire.domain.racing;

import za.co.yellowfire.DateUtil;
import za.co.yellowfire.manager.DomainManager;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Local(RaceManager.class)
@Remote(RaceManagerRemote.class)
@Stateless(
  name = "RaceManager", 
  mappedName = "yellowfire/session/RaceManager",
  description = "Manages the race related information")
public class RaceManagerBean implements RaceManager, RaceManagerRemote {
	private static final long serialVersionUID = 1L;
	//private static Logger LOGGER = LoggerFactory.getLogger(LogType.MANAGER.getCategory());

    @EJB private DomainManager manager;
    
    /**
     * Queries for the list of domain objects
     * @param domainQuery The domain query
     * @return List
     */
    /*
	protected List<?> query(DomainQuery domainQuery, Class<?> mappedClass) {
        Query query = em.createNativeQuery(domainQuery.getStatement(), mappedClass);
        if (domainQuery.getFirst() > 0) {
            query = query.setFirstResult(domainQuery.getFirst());
        }
        if (domainQuery.getMax() > 0) {
            query = query.setMaxResults(domainQuery.getMax());
        }
        return query.getResultList();
    }
    */

    /**
     * Retrieves the races that match the domain query
     * @return The races that match the domain query
     */
    @SuppressWarnings("unchecked")
    @Override public List<Race> retrieveUpcomingRaces() {	
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Race.FIELD_DATE, DateUtil.getDate(false, false));
        return (List<Race>) manager.query(Race.QRY_UPCOMING_RACES, params);
    }
    
    /**
     * Retrieves the races for a specified date
     * @param date The date of the races to retrieve
     * @return The races that match the date
     */
    @SuppressWarnings("unchecked")
    @Override public List<Race> retrieveRacesForDate(Date date) {	
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Race.FIELD_DATE, DateUtil.getDate(date, false, false));
        return (List<Race>) manager.query(Race.QRY_RACES_FOR_DATE, params);
    }
    
    /**
     * Retrieves the races for a specified id
     * @param id The id of the races to retrieve
     * @return The races that match the id
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Race> retrieveRacesForId(Long id) {
    	Map<String, Object> params = new HashMap<String, Object>(1);
        params.put(Race.FIELD_ID, id);
        return (List<Race>) manager.query(Race.QRY_RACES_FOR_ID, params);
    }
    
    /**
     * Retrieves the clubs
     * @return The clubs
     */
    @SuppressWarnings("unchecked")
    @Override public List<Club> retrieveClubs() {
        return (List<Club>) manager.query(Club.QRY_CLUBS, null);
    }
    
    /**
     * Retrieve a club by id
     * @param id The id of the club
     * @return Club
     */
    @Override
    public Club retrieveClub(Long id) {
    	return (Club) manager.find(Club.class, id);
    }
    
    /**
     * Retrieve a race by id
     * @param id The id of the race
     * @return Race
     */
    @Override
    public Race retrieveRace(Long id) {
    	return (Race) manager.find(Race.class, id);
    }
    
    /**
     * Registers a person for a race
     * @param raceRegistration The race registration
     * @return RaceRegistration
     */
    public RaceRegistration register(RaceRegistration raceRegistration) {
    	manager.persist(raceRegistration);
    	return raceRegistration;
    }
    
    
    public Race persistRace(Race race) {
        manager.persist(race);
        return race;
    }

    public Race mergeRace(Race race) {
        return (Race) manager.merge(race);
    }

    public void removeRace(Race race) {
        race = (Race) manager.find(Race.class, race.getId());
        manager.remove(race);
    }
}
