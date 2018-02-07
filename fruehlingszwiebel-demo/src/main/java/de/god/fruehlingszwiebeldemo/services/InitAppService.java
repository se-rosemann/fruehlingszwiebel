package de.god.fruehlingszwiebeldemo.services;

import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.CarCreationException;
import de.god.fruehlingszwiebeldemo.domain.car.WheelPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InitAppService {
    @Autowired
    private CarService carService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("hello world, I have just started up");
        // Fill the database with some entries

        Map<WheelPosition, String> tireTypes = new HashMap<>();
        tireTypes.put(WheelPosition.FRONT_LEFT, "Pirelli");
        tireTypes.put(WheelPosition.FRONT_RIGHT, "Pirelli");
        tireTypes.put(WheelPosition.REAR_LEFT, "Goodyear");
        tireTypes.put(WheelPosition.REAR_RIGHT, "Goodyear");

        // First car
        CarWriteModel carWriteModel = new CarWriteModel();
        carWriteModel.vehicleIdentificationNumber = "WAX-34TZ";
        carWriteModel.tireTypes = tireTypes;

        try {
            carService.createCar(carWriteModel);
        } catch (CarCreationException pE) {
            pE.printStackTrace();
        }

        // Second car
        CarWriteModel carWriteModel2 = new CarWriteModel();
        carWriteModel2.vehicleIdentificationNumber = "JAX-9812";
        carWriteModel2.tireTypes = tireTypes;

        try {
            carService.createCar(carWriteModel2);
        } catch (CarCreationException pE) {
            pE.printStackTrace();
        }
    }
}
