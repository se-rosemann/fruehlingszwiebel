package de.god.fruehlingszwiebeldemo.domain.car;

import de.god.fruehlingszwiebeldemo.api.car.CarReadModel;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.exception.CarCreationException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarDomainService {

    private CarRepository carRepository;

    public CarDomainService(CarRepository pCarRepository) {
        carRepository = pCarRepository;
    }

    /**
     * Factory-method to create a car - standard-way
     */
    public Car createCar(String pVehicleIdentificationNumber, Map<WheelPosition, String> tireTypes) throws CarCreationException {
        // Here we could do some initialization that should not be done in the constructure

        // ... and create the car using its constructor
        Car newCar = new Car(pVehicleIdentificationNumber, tireTypes);

        // Discussion: Should we use the Repository to make a save here in the Domain?
        // Maybe yes, because that can be done technology-agnostic
        return carRepository.saveCar(newCar);
    }

    /**
     * Factory-method to create a car - API-way
     */
    public Car createCar(CarWriteModel pCarWriteModel) throws CarCreationException {
        // Here we could do some initialization that should not be done in the constructure

        // ... and create the car using its constructor
        Car newCar = new Car(pCarWriteModel);

        // Discussion: Should we use the Repository to make a save here in the Domain?
        // Maybe yes, because that can be done technology-agnostic
        return carRepository.saveCar(newCar);
    }

    /**
     * Rotate the car, this method is artificially complex because it needs some transformation that
     * could be made obsolete by a smoother domain model
     *
     * @param carId
     * @param newPositionByTireID
     * @param mileAge
     */
    public void rotateWheels(UUID carId, Map<UUID, WheelPosition> newPositionByTireID, long mileAge) {
        // Get the car
        Car car = carRepository.findCarById(carId);

        // Map the UUID > Wheelposition-map to the real domain objects
        Map<WheelPosition, Tire> newPositions = new HashMap<>();
        for (Tire existingTire : car.getTires()) {
            WheelPosition newTirePosition = newPositionByTireID.get(existingTire.getId());
            newPositions.put(newTirePosition, existingTire);
        }

        // The business-method is part of the car
        car.rotateTires(newPositions, mileAge);

        // Save it
        carRepository.saveCar(car);
    }


    /**
     * Customer said: Every day im looking for the worst car in the past month
     */
    public CarReadModel findTheWorstCarPastMonth() {
        // The repository does the job
        return carRepository.findTheWorstCarPastMonth();
    }

    public static CarReadModel convertCar2CarReadModel(Car car) {
        CarReadModel carReadModel = new CarReadModel();
        carReadModel.vehicleIdentificationNumber = car.getVehicleIdentificationNumber();
        carReadModel.setVersion(car.getVersion());
        carReadModel.setId(car.getId());
        return carReadModel;
    }

}
