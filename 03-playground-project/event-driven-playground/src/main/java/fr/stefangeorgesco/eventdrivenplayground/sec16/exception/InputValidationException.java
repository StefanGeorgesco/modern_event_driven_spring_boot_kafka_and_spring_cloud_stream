package fr.stefangeorgesco.eventdrivenplayground.sec16.exception;

public class InputValidationException extends RuntimeException {

    private static final String MESSAGE = "Invalid orderId: %d";

    public InputValidationException(Integer orderId) {
        super(MESSAGE.formatted(orderId));
    }

}
