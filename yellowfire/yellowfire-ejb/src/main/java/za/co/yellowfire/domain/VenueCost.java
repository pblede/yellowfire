package za.co.yellowfire.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Embeddable
public class VenueCost implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic
    @Column(name = "venue_cost")
    private double baseCost;

    @Basic
    @Column(name = "venue_attendee_cost")
    private double attendeeCost;

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public double getAttendeeCost() {
        return attendeeCost;
    }

    public void setAttendeeCost(double attendeeCost) {
        this.attendeeCost = attendeeCost;
    }

    @Override
    public String toString() {
        return "VenueCost{" +
                "baseCost=" + baseCost +
                ", attendeeCost=" + attendeeCost +
                '}';
    }
}
