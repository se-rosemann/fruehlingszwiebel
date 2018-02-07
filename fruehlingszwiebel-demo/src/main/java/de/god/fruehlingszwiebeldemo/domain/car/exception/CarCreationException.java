package de.god.fruehlingszwiebeldemo.domain.car.exception;

public class CarCreationException extends Exception {

    public CarCreationException(Throwable cause) {
        super(cause);
    }

    public CarCreationException(String message) {
        super(message);
    }
}
