package za.co.yellowfire.domain.query;


/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class RaceDomainQuery extends AbstractDomainQuery {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean upcomingRaces;
    

    public RaceDomainQuery() {
        super();
    }
    
    public RaceDomainQuery(int first, int max) {
        super(first, max);
    }
    
  /**
   * Statement
   * @return The SQL statement for the query
   */
    public String getStatement() {
    
        final StringBuffer sql = new StringBuffer(1024).append("select ")
            .append("race_id,\n")
            .append("race_name,\n")
            .append("race_date,\n")
            .append("venue_id,\n")
            .append("graphic_link_id\n")
            .append("from rce.race r \n")
            .append("where 1=1\n");
        
        if (upcomingRaces) {
            sql.append("and r.race_date >= GETDATE()");
        }
        return sql.toString();
    }
}
