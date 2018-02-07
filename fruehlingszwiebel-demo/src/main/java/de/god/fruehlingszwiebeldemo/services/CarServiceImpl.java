package de.god.fruehlingszwiebeldemo.services;

import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService {

    private CarDomainService carDomainService;

    private CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository pCarRepository) {
        carRepository = pCarRepository;
        this.carDomainService = new CarDomainService(carRepository);
    }

    /**
     * Standard-way: Transaction to create and save a new car
     */
    @Override
    @Transactional
    public Car createCar(String pVehicleIdentificationNumber, Map<WheelPosition, String> tireTypes) throws CarCreationException {
        return carDomainService.createCar(pVehicleIdentificationNumber, tireTypes);
    }

    /**
     * API-way: Transaction to create and save a new car
     */
    @Override
    @Transactional
    public Car createCar(CarWriteModel pCarWriteModel) throws CarCreationException {
        return carDomainService.createCar(pCarWriteModel);
    }

    @Override
    @Transactional
    public void rotateWheels(UUID carId, Map<UUID, WheelPosition> newPositionByTireID, long mileAge) {
        carDomainService.rotateWheels(carId, newPositionByTireID, mileAge);
    }
}
