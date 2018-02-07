package de.god.fruehlingszwiebeldemo.domain.car;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * The wheel is an
 */
@Entity
public class Wheel extends de.god.fruehlingszwiebeldemo.domain.Entity {

    @Enumerated(EnumType.STRING)
    private WheelPosition wheelPosition;

    private Wheel() {
    }

    /**
     * A Wheel is constructed by assigning a position on a car
     *
     * @param pWheelPosition
     */
    Wheel(WheelPosition pWheelPosition) {
        wheelPosition = pWheelPosition;
    }

    public WheelPosition getWheelPosition() {
        return wheelPosition;
    }

    /**
     * Set Tire creates a relation-object (TireUsageInterval)
     */
    public void setTire(Tire tire)
    {

    }

}
