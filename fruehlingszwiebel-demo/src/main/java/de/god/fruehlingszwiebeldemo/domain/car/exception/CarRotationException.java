package de.god.fruehlingszwiebeldemo.domain.car.exception;

/**
 * Discussion: Make this a RuntimeException?
 * If the outer shells are programmed well, an exception of this kind should never happen.
 */
public class CarRotationException extends RuntimeException {
    public CarRotationException(String message) {
        super(message);
    }
}
