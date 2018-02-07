package de.god.fruehlingszwiebeldemo.api.car;

import de.god.fruehlingszwiebeldemo.domain.car.Car;
import de.god.fruehlingszwiebeldemo.domain.car.exception.CarCreationException;
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
    void rotateTires(UUID carId, Map<UUID, WheelPosition> newPositionByTireID, long mileAge);

    /**
     * Customer said: Every day im looking for the worst car in the past month
     */
    CarReadModel findTheWorstCarPastMonth();
}
