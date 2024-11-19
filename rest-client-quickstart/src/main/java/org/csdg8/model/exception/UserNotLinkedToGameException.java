package org.csdg8.model.exception;

public class UserNotLinkedToGameException extends RuntimeException {
    public UserNotLinkedToGameException() {
        super();
    }

    public UserNotLinkedToGameException(String message) {
        super(message);
    }

    public UserNotLinkedToGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLinkedToGameException(Throwable cause) {
        super(cause);
    }
}
