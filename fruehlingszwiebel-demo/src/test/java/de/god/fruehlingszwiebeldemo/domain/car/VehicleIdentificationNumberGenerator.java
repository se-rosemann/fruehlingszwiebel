package de.god.fruehlingszwiebeldemo.domain.car;

import java.util.UUID;

public class VehicleIdentificationNumberGenerator {

    /**
     * Generate a 10-digit random VehicleIdentificationNumber
     * @return
     */
    public static String generate() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
