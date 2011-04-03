package za.co.yellowfire.domain.racing;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

@Local
public interface RaceManager {

    /**
     * Retrieves the upcoming races
     * @return The upcoming races that match the domain query
     */
    List<Race> retrieveUpcomingRaces();
    
    /**
     * Retrieves the races for a specified date
     * @param date The date of the races to retrieve
     * @return The races that match the date
     */
    List<Race> retrieveRacesForDate(Date date);
    
    /**
     * Retrieves the races for a specified id
     * @param id The id of the races to retrieve
     * @return The races that match the id
     */
    List<Race> retrieveRacesForId(Long id);
    
    /**
     * Registers a person for a race
     * @param raceRegistration The race registration
     * @return RaceRegistration
     */
    RaceRegistration register(RaceRegistration raceRegistration);
    
    /**
     * Retrieves the clubs
     * @return The clubs
     */
    List<Club> retrieveClubs();
    
    /**
     * Retrieve a club by id
     * @param id The id of the club
     * @return Club
     */
    Club retrieveClub(Long id);
    
    /**
     * Retrieve a race by id
     * @param id The id of the race
     * @return Race
     */
    Race retrieveRace(Long id);
    
    Race persistRace(Race race);

    Race mergeRace(Race race);

    void removeRace(Race race);

	
}
