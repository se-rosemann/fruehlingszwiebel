package de.god.fruehlingszwiebeldemo.domain.car;

import javax.persistence.Entity;

/**
 * This class represents a usage interval. It may not overlap for a Tire.
 */
@Entity
public class TireUsageInterval extends de.god.fruehlingszwiebeldemo.domain.Entity {

    private long startingMiles;
    private Long endMiles = null;

    private TireUsageInterval() {
    }

    // end-miles are not given at creating of an Interval
    public TireUsageInterval(long pStartingMiles) {
        startingMiles = pStartingMiles;
    }

    public long getStartingMiles() {
        return startingMiles;
    }

    public Long getEndMiles() {
        return endMiles;
    }

    /**
     * At setting the endMiles check overlay
     *
     * @param pEndMiles
     */
    public void setEndMiles(Long pEndMiles) {
        if (pEndMiles != null && pEndMiles.longValue() < startingMiles) {
            throw new RuntimeException("EndMiles has to be greater or equal to startMiles");
        }
        endMiles = pEndMiles;
    }

    /**
     * Interval is active as long as endMiles are not set
     * @return
     */
    public boolean isActive() {
        return this.endMiles == null;
    }

    /**
     * MileAge = 0, if endMiles are not set.
     * @return
     */
    public long getMileAge() {
        if (getEndMiles() == null) {
            return 0;
        } else {
            return getEndMiles().longValue() - getStartingMiles();
        }
    }
}
