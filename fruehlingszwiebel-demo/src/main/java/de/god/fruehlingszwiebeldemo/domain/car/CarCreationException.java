package de.god.fruehlingszwiebeldemo.domain.car;

public class CarCreationException extends Exception {

    public CarCreationException(Throwable cause) {
        super(cause);
    }

    public CarCreationException(String message) {
        super(message);
    }
}
