package za.co.yellowfire.domain.geocode;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public enum GeocodeAddressComponentType {
    /**
     * street_address indicates a precise street address.
     * route indicates a named route (such as "US 101").
     * intersection indicates a major intersection, usually of two major roads.
     * political indicates a political entity. Usually, this type indicates a polygon of some civil administration.
     * country indicates the national political entity, and is typically the highest order type returned by the Geocoder.
     * administrative_area_level_1 indicates a first-order civil entity below the country level. Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels.
     * administrative_area_level_2 indicates a second-order civil entity below the country level. Within the United States, these administrative levels are counties. Not all nations exhibit these administrative levels.
     * administrative_area_level_3 indicates a third-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
     * colloquial_area indicates a commonly-used alternative name for the entity.
     * locality indicates an incorporated city or town political entity.
     * sublocality indicates an first-order civil entity below a locality
     * neighborhood indicates a named neighborhood
     * premise indicates a named location, usually a building or collection of buildings with a common name
     * subpremise indicates a first-order entity below a named location, usually a singular building within a collection of buildings with a common name
     * postal_code indicates a postal code as used to address postal mail within the country.
     * natural_feature indicates a prominent natural feature.
     * airport indicates an airport.
     * park indicates a named park.
     * point_of_interest indicates a named point of interest. Typically, these "POI"s are prominent local entities that don't easily fit in another category such as "Empire State Building" or "Statue of Liberty."

In addition to the above, address components may exhibit the following types:

     * post_box indicates a specific postal box.
     * street_number indicates the precise street number.
     * floor indicates the floor of a building address.
     * room indicates the room of a building address.
     */

    street_address,
    route,
    intersection,
    political,
    country,
    administrative_area_level_1,
    administrative_area_level_2,
    administrative_area_level_3,
    colloquial_area,
    locality,
    sublocality,
    neighborhood,
    premise,
    subpremise,
    postal_code,
    postal_code_prefix,
    natural_feature,
    airport,
    park,
    point_of_interest,
    post_box,
    street_number,
    floor,
    room
}
