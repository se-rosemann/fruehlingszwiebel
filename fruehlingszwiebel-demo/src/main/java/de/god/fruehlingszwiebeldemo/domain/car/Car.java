package de.god.fruehlingszwiebeldemo.domain.car;

import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;

/**
 * Invariants:
 * <p>
 * 1. A car has always 4 wheels attached
 * 2. Wheel-positions must not change
 */
@Entity
public class Car extends de.god.fruehlingszwiebeldemo.domain.Entity {

    private String vehicleIdentificationNumber;

    @OneToMany(targetEntity = Wheel.class, cascade = CascadeType.ALL)
    private List<Wheel> wheels = new ArrayList<>();

    @OneToMany(targetEntity = Tire.class, cascade = CascadeType.ALL)
    private List<Tire> tires = new ArrayList<>();

    private Car() {
    }

    /**
     * 1. WriteModel from API, chain constructor for ease
     * <p>
     * Invariant: 4 wheels initialized
     */
    public Car(CarWriteModel pCarWriteModel) throws CarCreationException {
        this(pCarWriteModel.vehicleIdentificationNumber, pCarWriteModel.tireTypes);
    }

    /**
     * 2. Constructor with params
     * <p>
     * Invariant: 4 wheels initialized
     */
    public Car(String pVehicleIdentificationNumber, Map<WheelPosition, String> tireTypes) throws CarCreationException {
        this.vehicleIdentificationNumber = pVehicleIdentificationNumber;

        // Create a wheel on every position
        this.initWheelsAtAllPositions(tireTypes);
    }

    /**
     * Inits the car with 4 wheels and tire
     *
     * @param tireTypes
     * @throws CarCreationException
     */
    private void initWheelsAtAllPositions(Map<WheelPosition, String> tireTypes) throws CarCreationException {
        // We are idempotent...
        if (!this.wheels.isEmpty()) {
            return;
        }

        // TireTypes must be set, otherwise cancel construction
        if (tireTypes == null || tireTypes.isEmpty()) {
            throw new CarCreationException("TireTypes must not be null or empty.");
        }

        // Every WheelPosition must have a tire
        if (!tireTypes.keySet().equals(new HashSet<>(Arrays.asList(WheelPosition.values())))) {
            throw new CarCreationException("Every WheelPosition must have a tire.");
        }

        // Init all wheels
        for (Map.Entry<WheelPosition, String> tireType : tireTypes.entrySet()) {
            this.initWheelAndTire(tireType.getKey(), tireType.getValue());
        }
    }

    private void initWheelAndTire(WheelPosition pWheelPosition, String tireType) throws CarCreationException {
        Wheel wheel = new Wheel(pWheelPosition);
        this.wheels.add(wheel);

        try {
            this.tires.add(new Tire(wheel, tireType));
        } catch (TireCreationException pE) {
            throw new CarCreationException(pE);
        }
    }

    /**
     * External service-method, this method preserves all invariants!!!
     *
     * @param newPositions
     */
    public void rotateTires(Map<WheelPosition, Tire> newPositions, long mileAge) {

            // Check invariant:
            // All wheel-positions must be assigned
            if (!newPositions.keySet().equals(new HashSet(Arrays.asList(WheelPosition.values())))) {
                throw new CarRotationException("All wheel-positions must be set");
            }

            // Change positions
            for (Map.Entry<WheelPosition, Tire> newPosition : newPositions.entrySet()) {

                Tire tire = newPosition.getValue();
                WheelPosition wheelPosition = newPosition.getKey();

                // Finish running Tire usages
                tire.finishActiveUsageInterval(mileAge);

                Wheel atWheel = this.getWheelAtPosition(wheelPosition);

            // Start new usage intervals
            tire.startUsageInterval(atWheel, mileAge);
        }
    }

    public String getVehicleIdentificationNumber() {
        return vehicleIdentificationNumber;
    }

    public void setVehicleIdentificationNumber(String pVehicleIdentificationNumber) {
        vehicleIdentificationNumber = pVehicleIdentificationNumber;
    }

    public List<Wheel> getWheels() {
        return wheels;
    }

    public List<Tire> getTires() {
        return tires;
    }

    private Wheel getWheelAtPosition(WheelPosition pWheelPosition) {
        return this.getWheels().stream().filter(w -> pWheelPosition.equals(w.getWheelPosition())).findFirst().get();
    }
}
