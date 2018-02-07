package de.god.fruehlingszwiebeldemo.domain.car;

import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.exception.CarCreationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class CarTest {

    @Test(expected = CarCreationException.class)
    public void testCarCreationFailed() throws CarCreationException {
        Car car = new Car(VehicleIdentificationNumberGenerator.generate(), new HashMap<>());
    }

    @Test
    public void testNewCarParameterWay() throws CarCreationException {
        Map<WheelPosition, String> initialTires = new HashMap<>();
        initialTires.put(WheelPosition.FRONT_LEFT, "FL - Pirelli");
        initialTires.put(WheelPosition.FRONT_RIGHT, "FR - Hankook");
        initialTires.put(WheelPosition.REAR_LEFT, "RL - Goodyear");
        initialTires.put(WheelPosition.REAR_RIGHT, "RR - Continental");

        Car car = new Car(VehicleIdentificationNumberGenerator.generate(), initialTires);
    }

    @Test
    public void testNewCarWriteModelWay() throws CarCreationException {

        CarWriteModel carWriteModel = new CarWriteModel();

        carWriteModel.vehicleIdentificationNumber = VehicleIdentificationNumberGenerator.generate();

        carWriteModel.tireTypes = new HashMap<>();
        carWriteModel.tireTypes.put(WheelPosition.FRONT_LEFT, "FL - Pirelli");
        carWriteModel.tireTypes.put(WheelPosition.FRONT_RIGHT, "FR - Hankook");
        carWriteModel.tireTypes.put(WheelPosition.REAR_LEFT, "RL - Goodyear");
        carWriteModel.tireTypes.put(WheelPosition.REAR_RIGHT, "RR - Continental");

        Car car = new Car(carWriteModel);
    }

    @Test
    public void testRotate() throws CarCreationException {
        CarWriteModel carWriteModel = new CarWriteModel();

        carWriteModel.vehicleIdentificationNumber = VehicleIdentificationNumberGenerator.generate();

        carWriteModel.tireTypes = new HashMap<>();
        carWriteModel.tireTypes.put(WheelPosition.FRONT_LEFT, "FL - Pirelli");
        carWriteModel.tireTypes.put(WheelPosition.FRONT_RIGHT, "FR - Hankook");
        carWriteModel.tireTypes.put(WheelPosition.REAR_LEFT, "RL - Goodyear");
        carWriteModel.tireTypes.put(WheelPosition.REAR_RIGHT, "RR - Continental");

        //
        long mileAge = 0;
        Car car = new Car(carWriteModel);

        //
        mileAge = 333;
        Map<WheelPosition, Tire> newPositions = new HashMap<>();

        // Dirty, we don't have a direct connection from wheel to actual tire
        // For this test, just rotate the tires somehow
        newPositions.put(WheelPosition.REAR_RIGHT, car.getTires().get(1));
        newPositions.put(WheelPosition.REAR_LEFT, car.getTires().get(3));
        newPositions.put(WheelPosition.FRONT_LEFT, car.getTires().get(2));
        newPositions.put(WheelPosition.FRONT_RIGHT, car.getTires().get(0));

        car.rotateTires(newPositions, mileAge);

        // Asserts...
    }
}
