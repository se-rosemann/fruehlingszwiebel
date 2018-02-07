package de.god.fruehlingszwiebeldemo.domain.car;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository {

    Car saveCar(Car car);

    Car findCarById(UUID carId);

    List<Car> findAllCars();
}
