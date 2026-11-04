package fr.stefangeorgesco.eventdrivenplayground.sec16.exception;

public class ServiceUnavailableException extends RuntimeException {

    private static final String MESSAGE = "Service unavailable";

    public ServiceUnavailableException() {
        super(MESSAGE);
    }

}
