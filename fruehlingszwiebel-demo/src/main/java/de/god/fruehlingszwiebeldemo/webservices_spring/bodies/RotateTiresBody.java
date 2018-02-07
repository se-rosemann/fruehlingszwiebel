package de.god.fruehlingszwiebeldemo.webservices_spring.bodies;

import de.god.fruehlingszwiebeldemo.domain.car.WheelPosition;

import java.util.Map;
import java.util.UUID;

public class RotateTiresBody {
    public UUID carId;
    public Map<UUID, WheelPosition> newPositionByTireID;
    public long mileAge;

    public RotateTiresBody() {
    }
}
