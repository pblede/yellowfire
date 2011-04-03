package za.co.yellowfire.domain.query;


public abstract class AbstractDomainQuery implements DomainQuery {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int first;
    private int max;
    
    /**
     * Constructs the domain query with no constraints
     */
    public AbstractDomainQuery() {
        this(0, 0);
    }

    /**
     * Constructs the domain query with constraints
     * @param first The first row in the results to return
     * @param max The maximum rows to return
     */
    public AbstractDomainQuery(int first, int max) {
        super();
        this.first = first;
        this.max = max;
    }


    /**
     * @return The first row in the results to return
     */
    public int getFirst() {
        return first;
    }

  /**
   * @return The maximum rows to return
   */
    public int getMax() {
        return max;
    }
}
