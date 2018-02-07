package de.god.fruehlingszwiebeldemo.repository;

import de.god.fruehlingszwiebeldemo.api.car.CarReadModel;
import de.god.fruehlingszwiebeldemo.domain.car.Car;
import de.god.fruehlingszwiebeldemo.domain.car.CarDomainService;
import de.god.fruehlingszwiebeldemo.domain.car.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the CarRepository from the domain using spring-data
 */
@Repository
public class CarRepositorySpringImpl implements CarRepository {

    /**
     * Standard-CrudRepository, hidden outside as private interface, so that it can't be accidentally injected
     */
    @Repository
    private interface CarRepositorySpring extends CrudRepository<Car, UUID> {
    }

    private CarRepositorySpring carRepositorySpring;

    @Autowired
    public CarRepositorySpringImpl(CarRepositorySpring pCarRepositorySpring) {
        carRepositorySpring = pCarRepositorySpring;
    }

    @Override
    public Car saveCar(Car car) {
        // Delegate to CrudRepository.save
        return carRepositorySpring.save(car);
    }

    @Override
    public Car findCarById(UUID carId) {
        // Delegate to CrudRepository.findOne
        return carRepositorySpring.findOne(carId);
    }

    @Override
    public CarReadModel findTheWorstCarPastMonth() {
        // Here we could execute a graph-query to fill a CarReadModel

        // For demonstration-purpose we convert the first Car we find
        Car firstCar = carRepositorySpring.findAll().iterator().next();
        return CarDomainService.convertCar2CarReadModel(firstCar);
    }

    @Override
    public List<Car> findAllCars() {
        List<Car> result = new ArrayList<>();
        // Just delegate to spring-data implementation
        carRepositorySpring.findAll().iterator().forEachRemaining(result::add);
        return result;
    }
}
