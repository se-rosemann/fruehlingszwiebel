package de.god.fruehlingszwiebeldemo.domain.car;

import de.god.fruehlingszwiebeldemo.api.car.CarReadModel;

import java.util.List;
import java.util.UUID;

public interface CarRepository {

    Car saveCar(Car car);

    Car findCarById(UUID carId);

    CarReadModel findTheWorstCarPastMonth();

    List<Car> findAllCars();
}
