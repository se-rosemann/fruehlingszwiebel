package de.god.fruehlingszwiebeldemo.api.car;

import de.god.fruehlingszwiebeldemo.domain.car.Car;
import de.god.fruehlingszwiebeldemo.domain.car.CarCreationException;
import de.god.fruehlingszwiebeldemo.domain.car.Tire;
import de.god.fruehlingszwiebeldemo.domain.car.WheelPosition;

import java.util.Map;
import java.util.UUID;

public interface CarService {

    /**
     * Create a car - standard way
     */
    Car createCar(String pVehicleIdentificationNumber, Map<WheelPosition, String> tireTypes) throws CarCreationException;

    /**
     * Create a car - API-way with WriteModel
     */
    Car createCar(CarWriteModel pCarWriteModel) throws CarCreationException;

    /**
     * Rotate the wheels
     */
    void rotateWheels(UUID carId, Map<UUID, WheelPosition> newPositionByTireID, long mileAge);
}
