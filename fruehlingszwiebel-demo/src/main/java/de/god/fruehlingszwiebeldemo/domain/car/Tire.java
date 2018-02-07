package de.god.fruehlingszwiebeldemo.domain.car;

import de.god.fruehlingszwiebeldemo.domain.car.exception.TireCreationException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Tire extends de.god.fruehlingszwiebeldemo.domain.Entity {
    private String type;

    @OneToMany(targetEntity = TireUsageInterval.class, cascade = CascadeType.ALL)
    private List<TireUsageInterval> usageIntervalList = new ArrayList<>();

    private Tire() {
    }

    public Tire(Wheel atWheel, String pType) throws TireCreationException {
        if (pType == null || pType.trim().isEmpty()) {
            throw new TireCreationException("pType must be set.");
        }
        type = pType;

        // New tire > first usage-interval at 0 miles
        this.startUsageInterval(atWheel, 0L);

    }

    public String getType() {
        return type;
    }

    /**
     * MileAge is the sum of all UsageIntervals
     *
     * @return
     */
    public long getMileAge() {
        return this.usageIntervalList.stream().map(u -> u.getMileAge()).reduce(0L, Long::sum);
    }

    public List<TireUsageInterval> getUsageIntervalList() {
        return Collections.unmodifiableList(usageIntervalList);
    }

    public void addUsageInterval(TireUsageInterval pTireUsageInterval) {
        if (!this.usageIntervalList.contains(pTireUsageInterval)) {
            this.usageIntervalList.add(pTireUsageInterval);
        }
    }

    public TireUsageInterval getActiveUsageInterval() {
        // TODO Null-Check
        return this.usageIntervalList.stream().filter(u -> u.isActive()).findAny().get();
    }

    public void finishActiveUsageInterval(long endMiles) {
        this.getActiveUsageInterval().setEndMiles(endMiles);
    }

    /**
     * Starts a using-interval
     *
     * @param atWheel  (not used at the moment, because we are not able to jsonifiy (transisitve) bidirectional relations
     * @param pMileAge
     */
    public void startUsageInterval(Wheel atWheel, long pMileAge) {
        TireUsageInterval newInterval = new TireUsageInterval(pMileAge);
        this.addUsageInterval(newInterval);
    }
}
