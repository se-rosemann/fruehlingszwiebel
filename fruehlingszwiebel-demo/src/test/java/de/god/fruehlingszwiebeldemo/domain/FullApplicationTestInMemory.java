package de.god.fruehlingszwiebeldemo.domain;


import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * In-memory-implementation for test-purposes
 */
@RunWith(JUnit4.class)
public class FullApplicationTestInMemory extends FullApplicationTest {

    // Im-memory CarRepository
    private CarRepository carRepository = null;

    private CarDomainService carDomainService = null;

    @Before
    public void beforeClass() {
        this.carRepository = new CarRepository() {
            // Save the cars in-memory, no cascades etc.
            Map<UUID, Car> cars = new HashMap<>();

            @Override
            public Car saveCar(Car car) {
                this.cars.put(car.getId(), car);
                return car;
            }

            @Override
            public Car findCarById(UUID carId) {
                Car foundCar = this.cars.get(carId);
                return foundCar;
            }

            @Override
            public List<Car> findAllCars() {
                return cars.values().stream().sorted((c1, c2) -> c1.getId().compareTo(c2.getId())).collect(Collectors.toList());
            }
        };
        this.carDomainService = new CarDomainService(carRepository);
    }

    @Override
    protected CarRepository getCarRepository() {
        return carRepository;
    }

    @Override
    protected CarService getCarService() {
        return new CarService() {
            @Override
            public Car createCar(String pVehicleIdentificationNumber, Map<WheelPosition, String> tireTypes) throws CarCreationException {
                // No transaction needed, just delegate to domainService
                return carDomainService.createCar(pVehicleIdentificationNumber, tireTypes);
            }

            @Override
            public Car createCar(CarWriteModel pCarWriteModel) throws CarCreationException {
                // No transaction needed, just delegate to domainService
                return carDomainService.createCar(pCarWriteModel);
            }

            @Override
            public void rotateWheels(UUID carId, Map<UUID, WheelPosition> newPositions, long mileAge) {
                // No transaction needed, just delegate to domainService
                carDomainService.rotateWheels(carId, newPositions, mileAge);
            }
        };
    }
}
