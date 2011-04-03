package za.co.yellowfire.domain.query;

import java.io.Serializable;

public interface DomainQuery extends Serializable {

    /**
     * @return The first row in the results to return
     */
    int getFirst();
  
    /**
    * @return The maximum rows to return
    */
    int getMax();
    
    /**
     * Statement
     * @return The SQL statement for the query
     */
    String getStatement();
}
