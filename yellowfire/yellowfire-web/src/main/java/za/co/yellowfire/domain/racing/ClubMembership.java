package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.profile.Profile;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Mark P Ashworth
 * @version 0.1.0
 */
public class ClubMembership {

    //@XmlElement(name = "person")
    //@ManyToOne(fetch = FetchType.EAGER, optional = true, targetEntity = Profile.class)
    //@JoinColumn(name = "person_id", nullable = true, updatable = true, referencedColumnName = "person_id")
    private Profile person;

    //@XmlElement(name = "club")
    //@ManyToOne(fetch = FetchType.EAGER, optional = true, targetEntity = Club.class)
    //@JoinColumn(name = "club_id", nullable = true, updatable = true, referencedColumnName = "club_id")
    private Club club;
}
