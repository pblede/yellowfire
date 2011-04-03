package za.co.yellowfire.domain.geocode;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public enum GeocodeStatus {
    /*
     * "OK" indicates that no errors occurred; the address was successfully parsed and at least one geocode was returned.
     * "ZERO_RESULTS" indicates that the geocode was successful but returned no results. This may occur if the geocode was passed a non-existent address or a latlng in a remote location.
     * "OVER_QUERY_LIMIT" indicates that you are over your quota.
     * "REQUEST_DENIED" indicates that your request was denied, generally because of lack of a sensor parameter.
     * "INVALID_REQUEST" generally indicates that the query (address or latlng) is missing.
     */
    OK,
    ZERO_RESULTS,
    OVER_QUERY_LIMIT,
    REQUEST_DENIED,
    INVALID_REQUEST
}
