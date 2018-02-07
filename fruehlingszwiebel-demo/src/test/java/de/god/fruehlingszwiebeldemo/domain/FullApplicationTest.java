package de.god.fruehlingszwiebeldemo.domain;

import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.*;
import de.god.fruehlingszwiebeldemo.domain.car.exception.CarCreationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class tests the whole composed workflow of the domain
 * Beware: The test in only runnable if implementations of the specified services are provided at runtime.
 */
public abstract class FullApplicationTest {

    protected abstract CarRepository getCarRepository();

    protected abstract CarService getCarService();

    @Test
    public void testApplicationWorkflow() throws CarCreationException {

        CarWriteModel carWriteModel = new CarWriteModel();
        carWriteModel.vehicleIdentificationNumber = VehicleIdentificationNumberGenerator.generate();
        Map<WheelPosition, String> tireTypes = new HashMap<>();
        tireTypes.put(WheelPosition.FRONT_LEFT, "Pirelli");
        tireTypes.put(WheelPosition.FRONT_RIGHT, "Pirelli");
        tireTypes.put(WheelPosition.REAR_LEFT, "Pirelli");
        tireTypes.put(WheelPosition.REAR_RIGHT, "Pirelli");
        carWriteModel.tireTypes = tireTypes;

        // This method should save the car
        Car car = getCarService().createCar(carWriteModel);

        // We should find the car
        Car foundCar = getCarRepository().findCarById(car.getId());
        Assert.assertEquals(car.getId(), foundCar.getId());
        Assert.assertEquals(car.getVehicleIdentificationNumber(), foundCar.getVehicleIdentificationNumber());

        // Rotate the wheels

        //
        long mileAge = 0;

        //
        mileAge = 333;
        Map<UUID, WheelPosition> newPositions = new HashMap<>();

        // Dirty, we don't have a direct connection from wheel to actual tire
        // For this test, just rotate the tires somehow
        newPositions.put(car.getTires().get(0).getId(), WheelPosition.REAR_RIGHT);
        newPositions.put(car.getTires().get(1).getId(), WheelPosition.FRONT_LEFT);
        newPositions.put(car.getTires().get(2).getId(), WheelPosition.REAR_LEFT);
        newPositions.put(car.getTires().get(3).getId(), WheelPosition.FRONT_RIGHT);

        getCarService().rotateTires(car.getId(), newPositions, mileAge);

        // Asserts...

    }
}
