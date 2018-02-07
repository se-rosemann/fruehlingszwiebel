package de.god.fruehlingszwiebeldemo.webservices_spring;

import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.api.car.CarWriteModel;
import de.god.fruehlingszwiebeldemo.domain.car.Car;
import de.god.fruehlingszwiebeldemo.domain.car.CarCreationException;
import de.god.fruehlingszwiebeldemo.domain.car.CarRepository;
import de.god.fruehlingszwiebeldemo.webservices_spring.bodies.RotateTiresBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "This is the Cars-API, enjoy it.")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CarController {

    private CarService carService;
    private CarRepository carRepository;

    @Autowired
    public CarController(CarService pCarService, CarRepository pCarRepository) {
        carService = pCarService;
        carRepository = pCarRepository;
    }

    @ApiOperation(value = "Find all the nice cars, naked object.",
            notes = "Cars are example-dummies and created at application-startup")
    @RequestMapping(value = "/api/car", method = RequestMethod.GET)
    public List<Car> cars() {
        return carRepository.findAllCars();
    }

    @ApiOperation(value = "Create a new car with wheels and tires.",
            notes = "To create a brand new car, Tire-Types and positions must be set.")
    @RequestMapping(value = "/api/car", method = RequestMethod.POST)
    public Car createCar(@RequestBody CarWriteModel pCarWriteModel) throws CarCreationException {
        return carService.createCar(pCarWriteModel);
    }

    @ApiOperation(value = "Rotate positions of the Tires.",
            notes = "All wheelPositions must be set.")
    @RequestMapping(value = "/api/car/rotatewheels", method = RequestMethod.POST)
    public void rotateWheels(@RequestBody RotateTiresBody pRotateTiresBody) throws CarCreationException {
        carService.rotateWheels(pRotateTiresBody.carId, pRotateTiresBody.newPositionByTireID, pRotateTiresBody.mileAge);
    }
}
