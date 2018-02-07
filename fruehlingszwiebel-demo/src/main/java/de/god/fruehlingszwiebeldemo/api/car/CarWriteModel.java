package de.god.fruehlingszwiebeldemo.api.car;

import de.god.fruehlingszwiebeldemo.domain.car.WheelPosition;

import java.util.Map;

public class CarWriteModel {
    public String vehicleIdentificationNumber;
    public Map<WheelPosition, String> tireTypes;
}
